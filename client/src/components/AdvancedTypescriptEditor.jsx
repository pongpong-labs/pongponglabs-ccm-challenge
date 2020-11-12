import React,{Component} from 'react';
import MonacoEditor from 'react-monaco-editor';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import {Uri} from 'monaco-editor/esm/vs/editor/editor.api';

import {files} from './typings';
import '../common.css';

import history from "./history.js";

const curLanguage = 'python';
const code =
`
`
const options = {
    minimap: { enabled: true },
    selectOnLineNumbers: true,
    cursorBlinking: "blink",
    cursorStyle: 'line',
    fontSize: 11,
    options: monaco.editor.IEditorConstructionOptions = {

    },
    model: monaco.editor.getModel(Uri.parse("file:///main.tsx"))
        ||
        monaco.editor.createModel(code, curLanguage, monaco.Uri.parse("file:///main.tsx")),
}


export class AdvancedTypescriptEditor extends Component {
    constructor(props){
        super(props);

        this.state = {
            code, curLanguage, options
        }

        this.reloadMonacoData();

        this.reloadLanguage();

        // this.reloadFontSize();
    }

    onMonacoChange(newValue, e) {
        window.localStorage.setItem('monaco-editor-online-value', newValue)
    }

    editorWillMount(monaco) {

        // validation settings
        monaco.languages.typescript.typescriptDefaults.setDiagnosticsOptions({
            noSemanticValidation: false,
            noSyntaxValidation: false
        });

        // compiler options
        monaco.languages.typescript.typescriptDefaults.setCompilerOptions({
            target: monaco.languages.typescript.ScriptTarget.ES6,
            allowNonTsExtensions: true
        });

        for (const fileName in files) {
            const fakePath = `file:///node_modules/@types/${fileName}`;

            monaco.languages.typescript.typescriptDefaults.addExtraLib(
                files[fileName],
                fakePath
            );
        }
    }

    editorDidMount(editor, monaco) {
        editor.focus();
    }

    render() {
        const languages = ['c', 'cpp', 'java','python'];
        const listItems = languages.map((item) =>
            <option value={item} key={item}>{item}</option>
        );

        const textSize = [11, 12, 13, 14, 15, 16];
        const textSizeItems = textSize.map((size) =>
            <option value = {size} key={size}>{size}</option>
        );

        function hashCode(s) {
            let h;
            for(let i = 0; i < s.length; i++)
                h = Math.imul(31, h) + s.charCodeAt(i) | 0;
        
            return h;
        }
        
        async function sendData(codeData, langData) {
            var hashData = hashCode(codeData)

            let xhr = new XMLHttpRequest();

            const json = {
                "fileName" : langData + hashData,
                "code" : codeData,
                "result" : "",
                "state" : "Ready"
            };

            xhr.open("POST", 'http://164.125.219.21:8888/upload-code');
            
            xhr.setRequestHeader('Content-Type', 'application/json');
            
            xhr.send(JSON.stringify(json));
            
            history.push("/result/" + langData + hashData);
        }

        return (
            <div>
                <div className="d-flex justify-content-center"> 
                    <span className="title">PNU Code Editor</span>
                    <button onClick={() => {
                            const code = window.localStorage.getItem('monaco-editor-online-value');
                            const langData = window.localStorage.getItem('monaco-editor-language-value');

                            sendData(code, langData);
                        }
                    }>
                        Send
                    </button>

                    <select className="language" value={this.state.curLanguage} onChange={this.handleLanguageChange}>
                        {listItems}
                    </select>

                    <select className="fontsize" value={this.state.options.fontSize} onChange={this.handleFontSizeChange}>
                        {textSizeItems}
                    </select>

                </div>
                <div className="momacoClass">
                    <MonacoEditor
                        language={this.state.curLanguage}
                        theme="vs-dark"
                        defaultValue=''
                        value={this.state.code}
                        onChange={this.onMonacoChange}
                        editorWillMount={this.editorWillMount}
                        editorDidMount={this.editorDidMount}
                        options={this.state.options}
                    />
                </div>
            </div>
        )
    }

    handleLanguageChange = e => {
        let originCode = '';
        
        if(e.target.value === "c") {
            originCode = 
`#include <stdio.h>

int main()
{
    printf("Hello World");

    return 0;
}`
        }

        if(e.target.value === "cpp") {
            originCode = 
`#include <iostream>

using namespace std;

int main()
{
    cout<<"Hello World";

    return 0;
}`
        }

        if(e.target.value === "java") {
            originCode = 
`package org.example;

public class Main
{
	public static void main(String[] args) {
		System.out.println("Hello World");
	}
}`
        }

        if(e.target.value === "python") {
            originCode = `print ('Hello World')`
        }

        this.setState({
            curLanguage: e.target.value,
            code: originCode 
        });

        window.localStorage.setItem('monaco-editor-language-value',e.target.value);
        window.localStorage.setItem('monaco-editor-online-value', originCode)

    };

    handleFontSizeChange = e => {
        this.setState({
            options: {
                ...this.state.options,
                fontSize: e.target.value
            }
        })
        window.localStorage.setItem('monaco-editor-language-value',e.target.value);       
    }

    reloadMonacoData(){
        const storeData = window.localStorage.getItem('monaco-editor-online-value');
        if(storeData){
            setTimeout(() => {
                this.setState({  code: storeData  })
            }, 100);
        }
    }

    reloadLanguage(){
        const langData = window.localStorage.getItem('monaco-editor-language-value');
        if(langData){
            setTimeout(() => {
                this.setState({  curLanguage: langData  })
            }, 100);
        }
    }

    reloadFontSize(){
        const sizeData = window.localStorage.getItem('monaco-editor-language-value')
        if(sizeData){
            setTimeout(() => {
                this.setState({ 
                    options: {
                    ...this.state.options,
                    fontSize: sizeData
                } 
            })
            }, 100);
        }
    }
}