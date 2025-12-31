#!/bin/sh

find ./src/ -type f \
  \( -name "*.java" \) \
  ! -path "*/dist/*" \
  -newermt "2025-12-10" \
  -exec sh -c '
    for file do
      echo "============================================================"
      echo "FILE: $file"
      echo "LAST_MODIFIED: $(stat -c "%y" "$file" | cut -d"." -f1)"
      echo "============================================================"
      echo
      sed "s/^/    /" "$file"
      echo
    done
  ' sh {} + > fontes.txt
