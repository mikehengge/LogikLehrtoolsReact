import React, { Component } from 'react';
import axios from 'axios';

// import logo from './logo.png';
import './App.css';

class App extends Component {
  constructor() {
    super();

    this.state = {
      value: '',
      heuristik: '',
      showMenu: true,
      data: null,
      result: null,
    };
    this.resultRef = React.createRef()
    this.stepsRef = React.createRef()
    this.handleChange = this.handleChange.bind(this);
    this.handleChange2 = this.handleChange2.bind(this);
    this.setResult = this.setResult.bind(this);
  }

  scrollToSteps = (event) => window.scrollTo(0, this.stepsRef.current.offsetTop)


  handleChange(event) {
    this.setState({value: event.target.value});
  }

  handleChange2(event) {
    this.setState({heuristik: event.target.value});
  }

  setResult  = (response) => {
    var unsatisfiable = 'unsatisfiable';
    if (response.match(unsatisfiable)) {
      this.setState({result: <div className="results" ref={this.resultRef}><h2>Result: Given formula is unsatisfiable.</h2><p><button className="button2" onClick={this.scrollToSteps}>Jump to steps</button></p></div>})
    }
    else this.setState({result: <div className="results" ref={this.resultRef}><h2>Result: Given formula is satisfiable.</h2><p><button className="button2" onClick={this.scrollToSteps}>Jump to steps</button></p></div>})

  }

  handleRes() {
    const url = 'http://192.168.133.129:6253';
    axios.get(url, {
      params: {
        'Content-Type': 'application/json',
        type: 'Resolution',
        formula: this.state.value,
        heuristik: this.state.heuristik,
        }
      })
      .then(function (response) {
        console.log("Response " + response.data);
        this.setState({data : response.data},
                    this.setResult(response.data),
                    window.scrollTo(0, this.resultRef.current.offsetTop));
      }.bind(this))
      .catch(function (error) {
        console.log(error);
      });
    console.log("Data: " + this.state.data);
  }
  handleBD() {
    const url = 'http://192.168.133.129:6253';
    axios.get(url, {
      params: {
        type: 'BD Resolution',
        formula: this.state.value,
        crossdomain: true,
    }});
    console.log('Click happened');
  }
  handleDP() {
    const url = 'http://192.168.133.129:6253';
    axios.get(url, {
      params: {
        type: 'DP',
        formula: this.state.value,
        crossdomain: true,
    }});
    console.log('Click happened');
  }


  render() {
    return (

        <header className={"dark"}>
      <div className="introduction">Welcome to Logik Lehrtools. This tool applies Resolution, BD Resolution or the DP algorithm on a logical formula and displays the result with all steps.</div>
      <div className="App">
          <label>Please enter your formula</label>
          <div className="inputFormula">
            <input type="text" id="formulaInput" autoFocus={true} value={this.state.value} onChange={this.handleChange}  placeholder="Example: (var1 AND var2) OR var3">
            </input>
          </div>
          <label>Please enter a heuristik (optional)</label>
          <div className="inputHeuristik">
            <input type="text2" id="heuristikInput" autoFocus={false} value={this.state.heuristik} onChange={this.handleChange2}  placeholder="Example: var1,var2,var3">
            </input>
          </div>
          <div className="menu">Choose an algorithm to be used on the formula<br></br>
            <button className="button2" onClick={this.handleRes.bind(this)}>Resolution</button>
            <button className="button2" onClick={this.handleBD.bind(this)}>BD Resolution</button>
            <button className="button2"onClick={this.handleDP.bind(this)}>DP</button>
          </div>
          <div>{this.state.result}</div>
          <div className="receivedResults" ref={this.stepsRef} dangerouslySetInnerHTML={{ __html: this.state.data}}></div>
      </div>
    </header>
    );
  }
}

export default App;
