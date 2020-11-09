import React,{Component} from 'react';
// import { RouteComponentProps } from "react-router-dom";

export class SonarqubeReportViewer extends Component {    
    render() {
        return(
            <div>
                <h1>{ this.props.match.params.code }</h1>
            </div>
        );
    }
}