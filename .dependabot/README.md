# How to use Dependabot

- By default, the repo created from cci template will include .dependabot folder and config.yml file.
- It runs scan daily to check if the dependency versions are up to date. If a newer version of dependency is avaiable, it will create PR automatically to target branch security-patch.
- After the builds on security-patch run successfully, you can merge the new version to master branch. 
- If you have a preferred target branch, change "target_branch" parameter in config.yml. 
- Dependabot needs the specified target branch to exist in order to the scan. 
- For more information about how it works, please check [how-it-works](https://dependabot.com/#how-it-works)
- For config.yml details, please check [config-file](https://dependabot.com/docs/config-file/)
