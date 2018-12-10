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
        </header>
      </div>
    );
  }
}

export default App;
