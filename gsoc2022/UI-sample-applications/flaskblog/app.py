from flask import Flask, request, render_template
app = Flask(__name__)
@app.route('/', methods =["GET", "POST"])
def index():    
    if request.method == "POST":
       # getting input with name = fname in HTML form
       fname = request.form.get("fname")
       # getting input with name = lname in HTML form
       lname = request.form.get("lname")       
       return "Welcome " + fname + " " + lname       
    return render_template("index.jinja2")
if __name__=='__main__':
   app.run()