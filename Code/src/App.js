//Importing React and Axios
import React, { Component } from 'react';
import axios from 'axios';

//Importing style file
import './App.css';

class Lehrtools extends Component {
  constructor() {
    super();
    //Different states with starting values
    this.state = {
      formula: '',
      heuristik: '',
      showMenu: true,
      data: null,
      result: null,
      steps: null
    };
    this.resultRef = React.createRef();
    this.handleFormula = this.handleFormula.bind(this);
    this.handleHeuristik = this.handleHeuristik.bind(this);
    this.setResult = this.setResult.bind(this);
    this.showSteps = this.showSteps.bind(this);
  }

  //Using input to formula field as new value for 'value'
  handleFormula(event) {
    this.setState({formula: event.target.value});
  }

  //Handling input in heuristic field on event (button press)
  handleHeuristik(event) {
    this.setState({heuristik: event.target.value});
  }

  //Handling the received result, cases are invalid input, unsatisfiable formula and satisfiable formula
  //Parameter 'response' is the text that the Server API has sent
  //Setting state result to new JSX-divs that will be displayed
  //In case of valid input a button that offers to show the steps will be displayed
  setResult  = (response) => {
    var invalid = 'invalid'
    var unsatisfiable = 'unsatisfiable';
    if (response.match(invalid)) { this.setState({result: <div className="results" ref={this.resultRef} ><h2>Given formula is not valid</h2></div>})} //case invalid input --> sets result to answer
    else if (response.match(unsatisfiable)) { //case unsatisfiable formula --> sets result to answer + result
      this.setState({result: <div className="results" ref={this.resultRef}><h2>Result: Given formula is unsatisfiable.</h2><p><button className="button2" onClick={this.showSteps}>Show steps</button></p></div>})
    } //case satisfiable formula --> sets result to answer + result
    else this.setState({result: <div className="results" ref={this.resultRef}><h2>Result: Given formula is satisfiable.</h2><p><button className="button2" onClick={this.showSteps}>Show steps</button></p></div>})
  }

  //Button 'Show steps' is clicked --> displays steps
  showSteps () {
    this.setState({steps: <div className="receivedResults" dangerouslySetInnerHTML={{ __html: this.state.data}}></div>});
  }


  //Used when button 'Resolution' is pressed
  handleRes() {
    this.setState({steps: null});
    const url = 'http://192.168.133.129:6253'; //URL of Server, Port where API is waiting
    axios.get(url, { //using axios to send GET-Request
      params: { //Send given input in parameters
        'Content-Type': 'application/json',
        type: 'Resolution',
        formula: this.state.formula,
        heuristik: this.state.heuristik,
        }
      })
      .then(function (response) { //working with the received response
        this.setState({data : response.data}, //set state of 'data' to data in response
                    this.setResult(response.data),//calling function setResult to display results
                    window.scrollTo(0,this.resultRef.current.offsetTop)); //scroll to where result is displayed
      }.bind(this))
      .catch(function (error) { //printing error if one happens
        console.log(error);
      });
  }
  //Used when button 'BD Resolution' is pressed, but no results are received (not implemented yet)
  handleBD() {
    this.setState({steps: null});
    const url = 'http://192.168.133.129:6253';
    axios.get(url, {
      params: {
        type: 'BD Resolution',
        formula: this.state.formula,
        heuristik: this.state.heuristik,
        crossdomain: true,
    }});
    console.log('Click happened');
  }
  //Used when button 'DP' is pressed, but no results are received (not implemented yet)
  handleDP() {
    this.setState({steps: null});
    const url = 'http://192.168.133.129:6253';
    axios.get(url, {
      params: {
        type: 'DP',
        formula: this.state.formula,
        heuristik: this.state.heuristik,
        crossdomain: true,
    }});
    console.log('Click happened');
  }

  //Rendering the components
  render() {
    return (
      <header className={"dark"}>
  	  {/*Introduction, plain JSX without functions*/}
      <div className="introduction">Welcome to Logik Lehrtools. This tool applies Resolution, BD Resolution or the DP algorithm on a logical formula and displays the result with all steps.</div>
      <div className="App">
          <label>Please enter your formula</label>
          <p>Use '+' for OR, '*' for AND, '-' for NOT</p>
          <div className="inputFormula">
      		{/*Input field for the formula, will be auto focused, displays the value of 'formula' and uses function handleFormula when something is written*/}
            <input type="text" id="formulaInput" autoFocus={true} value={this.state.formula} onChange={this.handleFormula}  placeholder="EXAMPLE: (x1+x2)*-x3">
            </input>
          </div>
          <label>Please enter a heuristik (optional)</label>
          <div className="inputHeuristik">
      		{/*Input field for the heuristic, displays the value of 'heuristic' and uses function handleHeuristic when something is written*/}
            <input type="text2" id="heuristikInput" autoFocus={false} value={this.state.heuristik} onChange={this.handleHeuristik}  placeholder="Example: var1,var2,var3">
            </input>
          </div>
      	  {/*Display the buttons, each will call its corresponding function when clicked*/}
          <div className="menu">Choose an algorithm to be used on the formula<br></br>
            <button className="button2" onClick={this.handleRes.bind(this)}>Resolution</button>
            <button className="button2" onClick={this.handleBD.bind(this)}>BD Resolution</button>
            <button className="button2"onClick={this.handleDP.bind(this)}>DP</button>
          </div>
      	  {/*Displaying value of 'result' and 'steps' --> answer and table with the steps if input was valid */}
          <div>{this.state.result}</div>
          <div>{this.state.steps}</div>
      </div>
    </header>
    );
  }
}

export default Lehrtools;
