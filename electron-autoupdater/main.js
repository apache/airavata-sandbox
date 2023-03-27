const { app, BrowserWindow, dialog } = require('electron')
const { autoUpdater } = require("electron-updater")
const isDev = require('electron-is-dev');

autoUpdater.logger = require('electron-log')
autoUpdater.logger.transports.file.level = 'info'

autoUpdater.on('checking-for-update', () => {
    console.log('Checking updates....')
})

autoUpdater.on('update-available', (info) => {
    console.log('update available')
    console.log('Version', info.version)
    console.log('ReleaseDate', info.releaseDate)
})

autoUpdater.on('download-progress', (progress) => {
    console.log(`Download in progress ${Math.floor(progress.percent)} out of 100`)
})

autoUpdater.on('update-downloaded', (info) => {
    const dialogOpts = {
        type: 'info',
        buttons: ['Restart', 'Later'],
        title: 'Application Update',
        message: info,
        detail:
            'A new version has been downloaded. Restart the application to apply the updates.',
    }
    
    dialog.showMessageBox(dialogOpts).then((returnValue) => {
        console.log(`Installing new application of version ${info.version}}`)
        if (returnValue.response === 0) autoUpdater.quitAndInstall()
    })
    autoUpdater.quitAndInstall()
})


autoUpdater.on('error', (error) => {
    console.error(error);
})


// check for updates every 1 minute
setInterval(() => {
    console.log('Checking for updates and install it if its a production app')
    if (!isDev) {
        autoUpdater.checkForUpdates();
    }
}, 60000)


const createWindow = () => {
    const win = new BrowserWindow({
        width: 800,
        height: 600,
    })
    win.loadFile('index.html')
}

app.on('ready', () => {
    createWindow()
})

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit()
    }
})