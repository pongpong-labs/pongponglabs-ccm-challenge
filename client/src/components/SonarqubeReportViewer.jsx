import React,{Component} from 'react';

let reportState = "Run"
let result = ""
let postCnt = 0;

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}

export class SonarqubeReportViewer extends Component {   
    constructor(props){
        super(props);

        this.state = {
            reportState,
            result
        }

        this.onStart()
    }

    onStart() {
        const scope = this

        const fileName = this.props.match.params.code;
        const url = "http://164.125.219.21:8888/state?fileName=" + fileName;    
        let xhr = new XMLHttpRequest();

        xhr.open("GET", url);

        xhr.onload = async function() {
            var data = JSON.parse(xhr.responseText);
            var msg = data["message"]

            scope.setState({
                reportState: msg,
                result: ""
            });

            if(msg !== "Finish" && msg !== "Fail" && postCnt++ < 20) {
                await delay(1000);
            
                xhr.open("GET", url);
                xhr.send();
            }
        
            if(msg === "Finish") {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // console.log(this.state.reportState)
                    scope.setState({
                        reportState: msg,
                        result: data["data"]
                    });
                }
            }
        };

        // if(this.state.reportState === "Run")
        xhr.send();
    }

    render() {
        return(
            <div>
                <h1>{ this.state.reportState }</h1>
                <textarea readOnly value={this.state.result} style={{ height: 1000, width: 1000 }} ></textarea>
            </div>
        );
    }
}