import React, {Component} from 'react';
import { Router, Route, Switch } from 'react-router-dom';
import './App.css';
import './common.css'
import {AdvancedTypescriptEditor} from "./components/AdvancedTypescriptEditor";
import {SonarqubeReportViewer} from "./components/SonarqubeReportViewer";

import history from './components/history';

class App extends Component {
    render() {
        return (
            <Router history={history}>
                <div>
                    <Route exact path="/" component={AdvancedTypescriptEditor}/>
                    
                    <Switch>
                        <Route path="/result/:code" component={SonarqubeReportViewer}/>
                        <Route path="/result" component={SonarqubeReportViewer}/>
                    </Switch>
                </div>
            </Router>
        );
    }
}

export default App;
