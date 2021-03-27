#!/bin/sh
cd c:/xampp/htdocs/demoproject
git config credential.helper store
git push --all

git add --all
timestamp() {
  date +"at %H:%M:%S on %d/%m/%Y"
}
git commit -am "Regular auto-commit $(timestamp)"
git push master