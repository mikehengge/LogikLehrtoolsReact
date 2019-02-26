import React, { Component } from 'react';
import axios from 'axios';

// import logo from './logo.png';
import './App.css';

class App extends Component {
  constructor() {
    super();

    this.state = {
      showMenu: false,
    };

    this.showMenu = this.showMenu.bind(this);
    this.closeMenu = this.closeMenu.bind(this);
  }



  showMenu(event) {
    event.preventDefault();

    this.setState({ showMenu: true }, () => {
      document.addEventListener('click', this.closeMenu);
    });
  }

  closeMenu(event) {

    if (!this.dropdownMenu.contains(event.target)) {

      this.setState({ showMenu: false }, () => {
        document.removeEventListener('click', this.closeMenu);
      });
    }
  }
  handleClick() {
    const fakeData = [ { formula: 'x1 + x2 * x3' } ];
    const url = 'http://192.168.133.129:80';
    axios.get(url, {
      topic: 'test',
      logs: fakeData, // look ma, no JSON.stringify()!
      crossdomain: true,
    });
    console.log('Click happened');
  }
  handleClick2() {
    const args = "test2";
    console.log('Click2 happened');
    axios.post(`https://jsonplaceholder.typicode.com/users`, { args })
      .then(res => {
        console.log(res);
        console.log(res.data);
      })
  }
  handleClick3() {
    const args = "test3";
    console.log('Click3 happened');
    axios.post(`https://jsonplaceholder.typicode.com/users`, { args })
      .then(res => {
        console.log(res);
        console.log(res.data);
      })
  }


  render() {
    return (

        <header className={"dark"}>
      <div className="introduction">Welcome to Logik Lehrtools. This tool applies Resolution, BD Resolution or the DP algorithm on a logical formula and displays the solution with all steps.</div>
      <div className="App">
          <label htmlFor="formulaInput">Please enter your formula</label>
          <div className="input">
            <input type="text" id="formulaInput" autoFocus={true} name="username"  placeholder="Example: (var1 AND var2) OR var3">
            </input>
            <button className="button" onClick={this.showMenu}>Go</button>
          </div>
          {
          this.state.showMenu
            ? (
              <div
                className="menu"
                ref={(element) => {
                  this.dropdownMenu = element;
                }}
              >
              Choose a algorithm to be used on the formula<br></br>
            <button className="button2" onClick={this.handleClick.bind(this)}>Resolution</button>
                <button className="button2" onClick={this.handleClick2.bind(this)}>BD Resolution</button>
                <button className="button2"onClick={this.handleClick3.bind(this)}>DP</button>
              </div>
            )
            : (
              null
            )
        }
      </div>
    </header>
    );
  }
}

export default App;
