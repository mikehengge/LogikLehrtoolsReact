import React, { Component } from 'react';
import logo from './logo.png';
import './App.css';


const test = ["test","test2","test3"]
class App extends Component {
  render() {
    const add = (x=5, y=10) => x+y;
    const test2 = test.map(test => test.toUpperCase())
    return (
      <div className="App">
        <header className={test[0].toLowerCase()}>
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            <div>{test2+" wurde getestet"}<br />Ergebnis der Rechnung:</div>
            <div>{add()}</div>
          </p>
        </header>
      </div>
    );
  }
}

export default App;
