import { Empty } from 'google-protobuf/google/protobuf/empty_pb';
import { ClientReadableStream } from 'grpc';
import React, { useEffect, useState } from 'react';
import './App.scss';
import logo from './logo.svg';
import { HealthServiceClient } from './proto/Health_serviceServiceClientPb';
import { Health, Heartbeat, Status } from './proto/health_service_pb';

const client = new HealthServiceClient("http://0.0.0.0:8080");

function App() {

  const [health, setHealth] = useState<Health|null>(null);
  const [beats, setBeats] = useState<Heartbeat[]>([]);
  const [isHearbeatOn, setHeartBeat] = useState<boolean>(false);

  var stream : ClientReadableStream<Heartbeat> | null = null;

  
  useEffect(() => {
    const request = new Empty();
    const metadata = {}
    client.getHealth(request, metadata)
      .then(data => {
        setHealth(data);
      })
      .catch(err => {
        console.log(err);
      })

  }, [])
  
  useEffect(() => {
    if(isHearbeatOn) {
      console.log("start streaming")
      stream = client.getHeartbeats(new Empty(), {}) as ClientReadableStream<Heartbeat>
      stream?.on('data', (chunk: Heartbeat) => {
        setBeats(beats => [...beats, chunk]);
      })
      .on('end', () => {
        console.log("end");
        const upVal = beats.filter((beat, idx, arr) => beat.getStatus() === Status.UP).reduce( (acc, val, arr) => acc + 1, 0)
        const downVal = beats.length - upVal

        console.log(`Up : ${upVal} , down : ${downVal}`);
      })
      .on('close', () => {
        console.log("closed")
      })
      .on('error', (err) => {
        console.log(err)
      })
      
    } else {
      console.log("stop streaming")
      stream?.cancel();
    }
  }, [isHearbeatOn])

  const toggleHeartbeatRequest = () => {
    setHeartBeat(!isHearbeatOn)
  }

  const isOnline = (health?.getStatus() === Status.UP) || false


  return (
    <div className="main">
      <header className="main-header">
        <img src={logo} className="header-brand-logo" alt="logo" />
        <span className="spacebar" />
        <span className={`health-indicator ${isOnline ? 'online' : 'offline'}`}></span>
      </header>
      {
        isOnline ?
        <div className="beats-browser">
          { !isHearbeatOn ? <button onClick={toggleHeartbeatRequest}>START</button> : ""}
          <div className="beats">
            {
              beats.map((beat, index) => (
                <span id={`row-id-${index}`} 
                  className={`beat ${beat.getStatus() === Status.UP ? "online" : "offline"}`} 
                  key={`row-id-${index}`}>{JSON.stringify(beat)}</span>
              ))
            }
          </div>
        </div> : 
        <div className="alert">Server is offline</div>
    }
    </div>
  );
}

export default App;
