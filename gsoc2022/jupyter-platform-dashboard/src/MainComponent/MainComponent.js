import React, { useState, useEffect } from "react";
import { Button, Input } from "antd";
import { Space, Table } from 'antd';
import 'antd/dist/antd.css';

const MainComponent = () => {
  const [flag, setFlag] = useState(false);
  const [name, setName] = useState("");
  const [memory, setMemory] = useState("");
  const [cpu, setCpu] = useState("");
  const [response, setResponse] = useState(null);
  const [table_data1, setData] = useState([]);
  const [table_data2, setData2] = useState([]);
  const columns1 = [
    {
      title: 'NB_id',
      dataIndex: 'nb_id',
      key: 'NB_id',
      render: (text) => <a>{text}</a>,
    },
    {
      title: 'Saved Time',
      dataIndex: 'saved_time',
      key: 'saved_time',
    },
    {
      title: 'Saved Location',
      dataIndex: 'saved_location',
      key: 'Saved_location',
    },
    {
      title: 'Action',
      key: 'action',
      render: (record) => (
        <Space size="middle">
          <Button onClick={() => runDB(record)}>Launch</Button>
          <Button onClick={() => saved_deleteDB(record)}>Delete</Button>
        </Space>
      ),
    },
  ];
  const saved_deleteDB = (record) => {
    console.log(record);
    fetch("http://127.0.0.1:5000/saved_delete", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(record.nb_id
      ),
    })
      .then((res) => res.json())
      .then((data) => {setData(table_data1.filter(item => item.nb_id !== record.nb_id))});
  }
  const run_deleteDB = (record) => {
    console.log(record);
    fetch("http://127.0.0.1:5000/run_delete", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(record.nb_id
      ),
    })
      .then((res) => res.json())
      .then((data) => {setData2(table_data2.filter(item => item.nb_id !== record.nb_id))});
  }
  const runDB = (record) => {
    console.log(record["nb_id"]);
    fetch('http://127.0.0.1:5000/launch', {
      method: 'POST',
      headers: {
        'Content-type': 'application/json',
      },
      body: JSON.stringify(record["nb_id"]),
    })
      .then((res) =>{
        if (res.status ===200){
          res.json().then(data=>{
            console.log(data);
            data["key"] = record["nb_id"];
            setData2(table_data2=>[...table_data2, data]);
          })
          // res.json().then(res => {
          //   fetch("http://127.0.0.1:5000/launch", {
          //     method: 'GET',
          //     headers: {
          //       'Content-type': 'application/json',
          //     }}).then((res) =>
          //   res.json().then((data) => setData2(convertJSON2toArray(data)))
          //   );
          // })
        }
      }
  )
      .catch(err=>{
        console.log(err);
      })
    

  };
  const columns2 = [
    {
      title: 'NB_id',
      dataIndex: 'nb_id',
      key: 'NB_id',
      render: (text) => <a>{text}</a>,
    },
    {
      title: 'Launched Time',
      dataIndex: 'running_time',
      key: 'running_time',
    },
    {
      title: 'Action',
      key: 'action',
      render: (record) => (
        <Space size="middle">
          <Button >Stop</Button>
          <Button onClick={() => run_deleteDB(record)}>Delete</Button>
        </Space>
      ),
    },
  ];
  const convertJSON2toArray = (json) => {
    let array = [];
    for (let i = 0; i < json.NB_id.length; i++) {
      array.push({"key": json.NB_id[i], "nb_id": json.NB_id[i], "running_time": json.running_time[i]});
    }
    return array;
  }
  const convertJSONtoArray = (json) => {
    let array = [];
    for (let i = 0; i < json.NB_id.length; i++) {
      array.push({"key": i, "nb_id": json.NB_id[i], "saved_time": json.saved_time[i], "saved_location": json.saved_location[i]});
    }
    return array;
  }
  useEffect(() => {
  fetch("http://127.0.0.1:5000", {
    method: 'GET',
    headers: {
      'Content-type': 'application/json',
    }}).then((res) =>
  res.json().then((data) => setData(convertJSONtoArray(data)))
  );
  fetch("http://127.0.0.1:5000/launch", {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
      }}).then((res) =>
    res.json().then((data) => setData2(convertJSON2toArray(data)))
    );
  }, []);
  
  const saveDB = () => {
    const data = {
      name: name,
      memory: memory,
      cpu: cpu,
    };


  
    fetch('http://127.0.0.1:5000', {
      method: 'POST',
      headers: {
        'Content-type': 'application/json',
      },
      body: JSON.stringify(data),
    })
      .then((res) =>
      res.json().then(res => setResponse(res)));
  };
  
  return (
    <>
    {!response &&
    <>
    <Button type="primary" onClick={() => setFlag(true)}>
        Create
      </Button>
      {flag && (
        <>
          <Input
            placeholder="Name"
            onChange={(Event) => setName(Event.target.value)}
          />
          <Input
            placeholder="Memory"
            onChange={(Event) => setMemory(Event.target.value)}
          />
          <Input
            placeholder="CPU"
            onChange={(Event) => setCpu(Event.target.value)}
          />
          <Button type="primary" onClick={() => saveDB()}>
            Submit
          </Button>
        </>
      )}
    </>}
      
      {response && 
      <>
        <p>{`Name ${response.name}`}</p>
        <p>{`Memory ${response.memory}`}</p>
        <p>{`CPU ${response.cpu}`}</p>
        </>
         }
        <div>Saved</div>
        <Table columns={columns1} dataSource={table_data1} />
        <div>Running</div>
        <Table columns={columns2} dataSource={table_data2} />
    </>
  );
};

export default MainComponent;
