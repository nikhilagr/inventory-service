---
name: pr-description
description: Write a pull request description. Use when asked to create a PR description, write PR notes, or summarize changes for a pull request.
allowed-tools: Bash
---
Steps:
1. Run git log main..HEAD --oneline to list commits
2. Run git diff main..HEAD --stat to see changed files
3. Write a PR description with these sections:
   ## Summary
   One paragraph: what changed and why.
   ## Changes
   Bullet list grouped by layer: domain, application, infrastructure, api.
   ## Testing
   What tests were added or modified.
   ## Notes
   Any breaking changes, migration steps, or reviewer callouts.
Keep it factual. No fluff.
