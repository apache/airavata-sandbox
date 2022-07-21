// Modules to control application life and create native browser window
//import fetch from "node-fetch";
//import { Menu, app, dialog, shell } from 'electron';
//import defaultMenu from 'electron-default-menu';
//import {app, BrowserWindow} from 'electron';
//import path from 'path';


const {app, BrowserWindow, MenuItem} = require('electron')
const path = require('path')
const { Menu, dialog, shell } = require('electron')
const defaultMenu = require('electron-default-menu')

function createWindow () {
  // Create the browser window.
  const mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  // and load the login page for app
  mainWindow.loadURL("https://seagrid.org/auth/login")

}

function createMolWindow () {
  // Create the browser window.
  const editorWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  // and load the login page for app
  //editorWindow.load("nanocad.html")
  editorWindow.loadURL("http://nglviewer.org/ngl/?script=showcase/ferredoxin")
}
function createJSMolWindow () {
  // Create the browser window.
  const JSMolWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  // and load the login page for app
  JSMolWindow.loadFile("C:\\Users\\aishw\\gsoc\\airavata-gsoc2022\\airavata-sandbox\\gsoc2022\\seagrid-rich-client\\ui\\samplemol.html")
  //editorWindow.loadURL("http://nglviewer.org/ngl/?script=showcase/ferredoxin")
}
// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.whenReady().then(() => {
  createWindow()

  app.on('activate', function () {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
  const menu = defaultMenu(app, shell);
  
 
  // Add custom menu
  menu.splice(1,0,{
    label: 'Molecule Viewer',
    submenu: [
      {
        label: 'NGLViewer',
        click: (item, focusedWindow) => {
          createMolWindow()
        }
      },
      {
        label: 'JSMol Viewer',
        click: (item, focusedWindow) => {
          createJSMolWindow()
        }
      }
    ]
  });
  menu.splice(2,0,{
    label: 'Applicaion Editor',
    submenu: [
      {
        label: 'G09',
        click: (item, focusedWindow) => {
          dialog.showMessageBox({message: 'Do something', buttons: ['OK'] });
        }
      }
    ]
  });
  
  
  // Set application menu
  Menu.setApplicationMenu(Menu.buildFromTemplate(menu));
})

// Quit when all windows are closed, except on macOS. There, it's common
// for applications and their menu bar to stay active until the user quits
// explicitly with Cmd + Q.
app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') app.quit()
})

