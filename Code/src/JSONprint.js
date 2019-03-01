import ReactDOM from 'react-dom';
import React, { Component } from 'react';

class JSONprint extends Component {
  render() {
    const{data} = this.props;
    return (<div><pre>{JSON.stringify(data, null, 2)}</pre></div>);
  }
}

ReactDOM.render(<JSONprint/>, document.getElementById('container'));

export default JSONprint;
