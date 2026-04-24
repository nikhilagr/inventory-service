#!/usr/bin/env bash
set -euo pipefail

# ── minikube ──────────────────────────────────────────────────────────────────
echo "==> Checking minikube..."
if ! minikube status --format '{{.Host}}' 2>/dev/null | grep -q "Running"; then
  echo "    Starting minikube..."
  minikube start --driver=docker --memory=4096
else
  echo "    minikube already running."
fi

# ── ingress addon ─────────────────────────────────────────────────────────────
echo "==> Checking ingress addon..."
if ! minikube addons list | grep -E "^.* ingress .*enabled" > /dev/null 2>&1; then
  echo "    Enabling ingress addon..."
  minikube addons enable ingress
  echo "    Waiting for ingress controller to be ready..."
  kubectl wait --namespace ingress-nginx \
    --for=condition=ready pod \
    --selector=app.kubernetes.io/component=controller \
    --timeout=90s
else
  echo "    Ingress addon already enabled."
fi

# ── minikube tunnel ───────────────────────────────────────────────────────────
echo "==> Opening minikube tunnel in a new Terminal tab..."
osascript <<'EOF'
tell application "Terminal"
  tell application "System Events" to keystroke "t" using command down
  delay 0.5
  do script "echo '── minikube tunnel ──' && minikube tunnel" in front window
end tell
EOF

# ── ArgoCD port-forward ───────────────────────────────────────────────────────
echo "==> Opening ArgoCD port-forward in a new Terminal tab..."
osascript <<'EOF'
tell application "Terminal"
  tell application "System Events" to keystroke "t" using command down
  delay 0.5
  do script "echo '── ArgoCD port-forward ──' && kubectl port-forward svc/argocd-server -n argocd 8443:443" in front window
end tell
EOF

# ── /etc/hosts entry ─────────────────────────────────────────────────────────
MINIKUBE_IP=$(minikube ip)
if ! grep -q "inventory.local" /etc/hosts; then
  echo "==> Adding inventory.local to /etc/hosts (requires sudo)..."
  echo "${MINIKUBE_IP} inventory.local" | sudo tee -a /etc/hosts > /dev/null
else
  echo "==> inventory.local already in /etc/hosts."
fi

# ── summary ───────────────────────────────────────────────────────────────────
echo ""
echo "┌─────────────────────────────────────────────────────────┐"
echo "│              Local Kubernetes environment ready          │"
echo "├─────────────────────────────────────────────────────────┤"
printf "│  %-20s  %-34s│\n" "Cluster"       "$(kubectl config current-context)"
printf "│  %-20s  %-34s│\n" "Minikube IP"   "${MINIKUBE_IP}"
printf "│  %-20s  %-34s│\n" "Inventory API" "http://inventory.local/api/v1/products"
printf "│  %-20s  %-34s│\n" "ArgoCD UI"     "https://localhost:8443"
printf "│  %-20s  %-34s│\n" "ArgoCD user"   "admin"
echo "├─────────────────────────────────────────────────────────┤"
echo "│  ArgoCD initial password:                               │"
echo "│    kubectl get secret argocd-initial-admin-secret \\    │"
echo "│      -n argocd -o jsonpath='{.data.password}' | base64 -d │"
echo "└─────────────────────────────────────────────────────────┘"
