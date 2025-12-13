package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"time"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/dialog"
	"fyne.io/fyne/v2/driver/desktop"
	"fyne.io/fyne/v2/layout"
	"github.com/airavata-sandbox/go-demo-app/helper"

	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"

	"fyne.io/fyne/v2/data/binding"
)

var client *http.Client
var publickeypath string

type typeToken struct {
	Text string `plain:"text"`
}

func getToken(userfield string, passfield string) (typeToken, error){
	var mytoken typeToken
	req, err := http.NewRequest("GET", "http://localhost:8081/login", nil)
	if err != nil{
		return typeToken{}, err
	}
	req.SetBasicAuth(userfield, passfield)
	resp, err := client.Do(req)
	if err != nil{
		return typeToken{}, err
	}

	if resp.StatusCode == 401{
		return typeToken{}, errors.New("invalid credentials")
	}

	bodyText, err := ioutil.ReadAll(resp.Body)
	if err != nil{
		return typeToken{}, err
	}

	mytoken.Text=string(bodyText)
	log.Println("token: ", mytoken.Text)
	return mytoken, nil
}

func validateToken(tokenstring string) (bool, error){
	publickeypath = "keys/public.pem"
	var isvalid bool

	v, err := helper.NewValidator(publickeypath)
	if err != nil {
		return false, errors.New("unable to validate")
	}
	token, err := v.GetToken(tokenstring)
	if err != nil {
		return false, errors.New("unable to validate")
	}
	log.Println(token.Claims)

	isvalid=true
	return isvalid, nil
}


func loadJsonData() []string {
	fmt.Println("Loading data from JSON file")

	input, _ := ioutil.ReadFile("data.json")
	var data []string
	json.Unmarshal(input, &data)

	return data
}

func saveJsonData(data binding.StringList) {
	fmt.Println("Saving data to JSON file")
	d, _ := data.Get()
	jsonData, _ := json.Marshal(d)
	ioutil.WriteFile("data.json", jsonData, 0644)

}

func onSignIn(){
	
}

func main() {
	myApp := app.New()

	// w := myApp.NewWindow("SysTray")

	if desk, ok := myApp.(desktop.App); ok {
		m := fyne.NewMenu("MyApp",
			fyne.NewMenuItem("Show", func() {
				// w.Show()
			}))
		desk.SetSystemTrayMenu(m)
	}

	// w.SetContent(widget.NewLabel("Fyne System Tray"))
	// w.SetCloseIntercept(func() {
	// 	w.Hide()
	// })
	// w.ShowAndRun()
	
	loginWindow := myApp.NewWindow("Login")
	user := widget.NewEntry()
	pass := widget.NewMultiLineEntry()

	form := &widget.Form{
		SubmitText: "Login",
		CancelText: "Cancel",
		Items: []*widget.FormItem{ // we can specify items in the constructor
			{Text: "Username", Widget: user},
			{Text: "Password", Widget: pass}},
		OnSubmit: func() { // optional, handle form submission
			client = &http.Client{Timeout: 10 * time.Second}

			log.Println("Form submitted:", user.Text)
			log.Println("multiline:", pass.Text)
			token, err := getToken(user.Text, pass.Text)
			if err!=nil{
				dialog.ShowError(err, loginWindow)
			} else {
				isvalid, err := validateToken(token.Text)
				if err != nil{
					dialog.ShowError(err, loginWindow)
				} else if !isvalid {
					dialog.ShowError(errors.New("invalid token"), loginWindow)
				} else {
					log.Println("login successful!", token)
					loginWindow.Close()
				}
				
			}
		},
		// OnCancel: func() {		
		// 	log.Println("Canceled")
		// 	entry.Refresh()
		// },
	}
	
	// form.OnCancel = func(){
	// 	form.Refresh()
	// }

	// // we can also append items
	// form.Append("Password", textArea)

	loginWindow.SetContent(form)
	// loginWindow.Resize(fyne.NewSize(400, 600))
	// loginWindow.Show()

	myWindow := myApp.NewWindow("List Data")

	loadedData := loadJsonData()

	data := binding.NewStringList()
	data.Set(loadedData)

	defer saveJsonData(data)

	list := widget.NewListWithData(data,
		func() fyne.CanvasObject {
			return widget.NewLabel("template")
		},
		func(i binding.DataItem, o fyne.CanvasObject) {
			o.(*widget.Label).Bind(i.(binding.String))
		})

	list.OnSelected = func(id widget.ListItemID) {
		list.Unselect(id)
		d, _ := data.GetValue(id)
		w := myApp.NewWindow("Edit Data")

		itemName := widget.NewEntry()
		itemName.Text = d

		updateData := widget.NewButton("Update", func() {
			data.SetValue(id, itemName.Text)
			w.Close()
		})

		cancel := widget.NewButton("Cancel", func() {
			w.Close()
		})

		deleteData := widget.NewButton("Delete", func() {
			var newData []string
			dt, _ := data.Get()

			for index, item := range dt {
				if index != id {
					newData = append(newData, item)
				}
			}

			data.Set(newData)

			w.Close()
		})

		w.SetContent(container.New(layout.NewVBoxLayout(), itemName, updateData, deleteData, cancel))
		w.Resize(fyne.NewSize(400, 200))
		w.CenterOnScreen()
		w.Show()

	}

	add := widget.NewButton("Add", func() {
		w := myApp.NewWindow("Add Data")

		itemName := widget.NewEntry()

		addData := widget.NewButton("Add", func() {
			data.Append(itemName.Text)
			w.Close()
		})

		cancel := widget.NewButton("Cancel", func() {
			w.Close()
		})

		w.SetContent(container.New(layout.NewVBoxLayout(), itemName, addData, cancel))
		w.Resize(fyne.NewSize(400, 200))
		w.CenterOnScreen()
		w.Show()

	})

	exit := widget.NewButton("Quit", func() {

		myWindow.Close()
	})

	myWindow.SetContent(container.NewBorder(widget.NewButton("Login", func() {
		loginWindow.Show()
	}), container.New(layout.NewVBoxLayout(), add, exit), nil, nil, list))
	myWindow.Resize(fyne.NewSize(400, 600))
	myWindow.SetMaster()
	myWindow.CenterOnScreen()
	myWindow.ShowAndRun()

}
