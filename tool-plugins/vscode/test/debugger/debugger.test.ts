'use strict';
/**
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

// The module 'assert' provides assertion methods from node

import assert = require('assert');
import * as Path from 'path';
import * as http from 'http';

import { DebugClientEx } from './debugClient';

import { getBallerinaHome } from '../test-util';
const ballerinaHome = getBallerinaHome();

suite('Ballerina Debug Adapter', () => {


    const PROJECT_ROOT = Path.join(__dirname, '../../../');

    const DEBUG_ADAPTER = Path.join(PROJECT_ROOT, 'out/src/debugger/index.js');
    const DATA_ROOT = Path.join(PROJECT_ROOT, 'test/data/');


    let dc: DebugClientEx;

    setup(() => {
        dc = new DebugClientEx('node', DEBUG_ADAPTER, 'ballerina', {cwd : PROJECT_ROOT });
        dc.defaultTimeout = 15000;
        return dc.start();
    });

    teardown( () => {
        dc.stop();
    });

    suite('vscode debugger integration tests', () => {
        test('Initialize request', () => {
            return dc.initializeRequest().then((response) => {
                response.body = response.body || {};
                assert.equal(response.body.supportsConfigurationDoneRequest, true);
            });
        });

        test('launch request', () => {
            const PROGRAM = Path.join(DATA_ROOT, 'hello_world.bal');
            dc.launch({ 
                script: PROGRAM, 
                "ballerina.home": ballerinaHome, 
                request: "launch", 
                name: "Ballerina Debug",
            });
            return dc.waitForEvent("initialized");
        }).timeout(5000);

        test('should stop on a breakpoint, main function', () => {
            const PROGRAM = Path.join(DATA_ROOT, 'hello_world.bal');

            const launchArgs = { 
                script: PROGRAM, 
                "ballerina.home": ballerinaHome, 
                request: "launch", 
                name: "Ballerina Debug",
            };
            
            return dc.hitBreakpoint(launchArgs, { path: PROGRAM, name: 'hello_world.bal', line: 4 });
        }).timeout(10000);

        test('should stop on a breakpoint, hello world service', () => {
            const PROGRAM = Path.join(DATA_ROOT, 'hello_world_service.bal');

            const launchArgs = { 
                script: PROGRAM, 
                "ballerina.home": ballerinaHome, 
                request: "launch", 
                name: "Ballerina Debug",
            };

            dc.launch(launchArgs).then(() =>{
                return Promise.all([
                    dc.setBreakpointsRequest({
                        breakpoints: [{ line: 11 }],
                        source: { path: PROGRAM, name: 'hello_world_service.bal'},
                    }),
                    dc.configurationDoneRequest(),
                    ]
                );
            }).then(()=>{
                setTimeout(()=>{
                    http.get('http://0.0.0.0:9090/hello/sayHello');
                }, 4000);
            });
    
            return dc.waitForEvent('stopped', 10000).then(event => {
                assert.equal(event.body.reason, "breakpoint");
            });

        }).timeout(15000);
    });

});