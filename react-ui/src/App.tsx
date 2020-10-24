import Button from '@material-ui/core/Button';
import { LocalHospital } from '@material-ui/icons';
import { Empty } from 'google-protobuf/google/protobuf/empty_pb';
import React, { useEffect, useState } from 'react';
import './App.scss';
import logo from './logo.svg';
import { Health, Heartbeat, Status } from './proto/health_service_pb';
import { HealthServiceClient, ResponseStream } from './proto/health_service_pb_service';

const client = new HealthServiceClient("http://0.0.0.0:8080");

function App() {

  const [health, setHealth] = useState<Health|null>(null);
  const [beats, setBeats] = useState<Heartbeat[]>([]);
  const [isHearbeatOn, setHeartBeat] = useState<boolean>(false);

  var stream: ResponseStream<Heartbeat> | null = null;

  
  useEffect(() => {
    const request = new Empty();
    const metadata = {}
    client.getHealth(request, (err, resp) => {
      if(err) console.log(err)
      else {
        setHealth(resp);
      }
    })

  }, [])
  
  useEffect(() => {
    if(isHearbeatOn) {
      console.log("start streaming")
      stream = client.getHeartbeats(new Empty())
      stream?.on('data', (chunk: Heartbeat) => {
        setBeats(beats => [...beats, chunk]);
      })
      .on('end', () => {
        //toggleHeartbeatRequest()
      })
      .on('status', (status) => {
        console.log(status);
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
          { 
            !isHearbeatOn ? 
            <Button variant="contained" 
              startIcon={ <LocalHospital />} 
              onClick={toggleHeartbeatRequest}
            >START</Button> 
            : <div className="stats">
                <span className="material-icons" style={{color: '#008800'}}>favorite</span>
                <span>{beats.filter((beat, idx, arr) => beat.getStatus() === Status.UP).length}</span>
                <span className="material-icons" style={{color: '#CC0000'}}>favorite</span>
                <span>{beats.filter((beat, idx, arr) => beat.getStatus() === Status.DOWN).length}</span>
              </div>
          }
          <div className="beats">
            {
              beats.map((beat, index) => (
                <div key={`beat-id-${index}`} className={`beat ${beat.getStatus() === Status.UP ? "online" : "offline"}` }>
                  <span className="id">{index + 1}</span>
                  <span className="detail">
                      {JSON.stringify(beat)}
                  </span>
                </div>
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
