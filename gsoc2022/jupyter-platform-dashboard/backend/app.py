from flask import Flask, request, render_template, jsonify
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import func
from flask_cors import CORS

app = Flask(__name__)
CORS(app)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://username:password@host:port/database_name'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

class Created(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), nullable=False)
    memory = db.Column(db.String(100), nullable=False)
    cpu = db.Column(db.String(100), nullable=False)
      
    def __repr__(self):
        return f'<Notebook {self.name}>'


class Saved(db.Model):
    NB_id = db.Column(db.Integer, primary_key=True)
    SavedTime = db.Column(db.DateTime(timezone=True),
                           server_default=func.now())
    SavedLocation = db.Column(db.Text)

    def __repr__(self):
        return f'<Notebook {self.id}>'


class Running(db.Model):
    NB_id = db.Column(db.Integer, primary_key=True)
    RunningTime = db.Column(db.DateTime(timezone=True),
                            server_default=func.now())
    
    def __repr__(self):
          return f'<Notebook {self.id}>'
    
@app.route('/', methods =["GET", "POST"])
def create():

    if request.method == "POST":
       print("POST request")
       data = request.get_json()
       # getting input with name = name in HTML form
       name = data['name']
       
       # getting input with name = memory in HTML form
       memory = data['memory']
       # getting input with name = cpu in HTML form
       cpu = data['cpu']

       created = Created(name=name,
                          memory=memory,
                          cpu=cpu
                        )
       db.create_all()
       db.session.add(created)
       db.session.commit()      
       return {"name": name, "memory": memory, "cpu": cpu}

    saved = Saved.query.all() 
    return {
        "NB_id": [i.NB_id for i in saved],
        "saved_time": [i.SavedTime for i in saved],
        "saved_location": [i.SavedLocation for i in saved]
    }
    

@app.route('/launch', methods =["GET", "POST"])
def launch():

    if request.method == "POST":
       data = request.get_json()
       NB_id = data
       running = Running(NB_id=NB_id)
       db.create_all()
       db.session.add(running)
       db.session.commit() 
       print(running.RunningTime)     
       return {"nb_id": data,'running_time': running.RunningTime}
    
    run = Running.query.all()
    return {
        "NB_id": [i.NB_id for i in run],
        "running_time": [i.RunningTime for i in run]
    }

@app.route('/saved_delete', methods =["POST"])
def saved_delete():
    
    if request.method == "POST":
        data = request.get_json()
        NB_id = data
        saved = Saved.query.get_or_404(NB_id)
        db.session.delete(saved)
        db.session.commit()
        return {"nb_id": data}
@app.route('/run_delete', methods =["POST"])
def run_delete():
    
    if request.method == "POST":
        data = request.get_json()
        NB_id = data
        running = Running.query.get_or_404(NB_id)
        db.session.delete(running)
        db.session.commit()
        return {"nb_id": data}
if __name__=='__main__':
   app.run()