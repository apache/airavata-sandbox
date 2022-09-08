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
var {spawn} = require('child_process')
var child = require('child_process').execFile;
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
  JSMolWindow.loadFile("C:\\Users\\aishw\\gsoc\\seagrid-client\\airavata-sandbox\\gsoc2022\\seagrid-rich-client\\ui\\samplemol.html")
}
function createJSMEWindow(){
  const JSMEWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  // and load the login page for app
  JSMEWindow.loadFile("C:\\Users\\aishw\\gsoc\\seagrid-client-electron\\airavata-sandbox\\gsoc2022\\seagrid-rich-client\\JSME\\dist\\index.html")
}
function createMol3DWindow(){
  const Mol3DWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  // and load the login page for app
  
  Mol3DWindow.loadURL("https://molview.org/")
}
function createAvogadro(){
    
        var executablePath = 'C:\\Program Files\\Avogadro2\\bin\\avogadro2.exe';
        var parameters = ['Hai', 'Test', 'Dat'];
        child(executablePath, function (err, data) {
            console.log(err)
            console.log(data.toString());
        });
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
    label: 'Molecule Editor',
    submenu: [
      {
        label: 'JSME Editor',
        click: (item, focusedWindow) => {
          createJSMEWindow()
        }
      },
      {
        label: 'Mol3DEditor',
        click: (item, focusedWindow) => {
          createMol3DWindow()
        }
      }
    ]
  });
  menu.splice(2,0,{
    label: 'Avogadro Application',
    submenu: [
      {
        label: 'Avogadro Editor',
        click: (item, focusedWindow) => {
          createAvogadro()
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

