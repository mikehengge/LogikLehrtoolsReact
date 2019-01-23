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
    const args = "test";
    console.log('Click happened');
    axios.post(`https://jsonplaceholder.typicode.com/users`, { args })
      .then(res => {
        console.log(res);
        console.log(res.data);
      })
  }


  render() {
    return (

        <header className={"dark"}>
      <div class="introduction">Welcome to Logik Lehrtools. This tool applies Resolution, BD Resolution or the DP algorithm on a logical formula and displays the solution with all steps.</div>
      <div className="App">
          <label for="formulaInput">Please enter your formula</label>
          <div className="input">
            <input type="text" id="formulaInput" autoFocus="true" name="username"  placeholder="Example: (var1 AND var2) OR var3">
            </input>
            <button class="button" onClick={this.showMenu}>Go</button>
          </div>
          {
          this.state.showMenu
            ? (
              <div
                class="menu"
                ref={(element) => {
                  this.dropdownMenu = element;
                }}
              >
              Choose a algorithm to be used on the formula<br></br>
            <button class="button2" onClick={this.handleClick.bind(this)}>Resolution</button>
                <button class="button2" >BD Resolution</button>
                <button class="button2" >DP</button>
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
