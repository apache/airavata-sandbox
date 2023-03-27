- POC on AutoUpdater with electron-builder

- Releases can be published at github, bintray, s3, digitalocean.

- Currently hosting Mac dmg and pkg releases.


How to test autoupdater on releases:

1. Make sure you have npm 9.X.X installed 
2. npm install 
3. Create a github developer token from github profile settings > developer_settings 
4. Change the version of the app in package.json 
5. Hit GH_TOKEN=<github_token> npm run release

