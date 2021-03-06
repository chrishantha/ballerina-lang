// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/runtime;
import ballerina/io;
import ballerina/streams;

type Teacher record {
    string name;
    int age;
    string status;
    string school;
    int count;
};

int index = 0;
stream<Teacher> inputStream;
stream<Teacher > outputStream;
Teacher[] globalEmployeeArray = [];

function startLengthWindowTest() returns (Teacher[]) {

    Teacher[] teachers = [];
    Teacher t1 = { name: "Mohan", age: 30, status: "single", school: "Hindu College" };
    Teacher t2 = { name: "Raja", age: 45, status: "single", school: "Hindu College" };
    Teacher t3 = { name: "Naveen", age: 35, status: "single", school: "Hindu College" };
    Teacher t4 = { name: "Amal", age: 50, status: "married", school: "Hindu College" };
    Teacher t5 = { name: "Nimal", age: 55, status: "married", school: "Hindu College" };
    Teacher t6 = { name: "Kavindu", age: 55, status: "married", school: "Hindu College" };

    teachers[0] = t1;
    teachers[1] = t2;
    teachers[2] = t3;
    teachers[3] = t4;
    teachers[4] = t5;
    teachers[5] = t6;

    testLengthWindow();

    outputStream.subscribe(printTeachers);
    foreach t in teachers {
        inputStream.publish(t);
    }

    runtime:sleep(1000);
    io:println(globalEmployeeArray);
    return globalEmployeeArray;
}

function testLengthWindow() {

    forever {
        from inputStream window lengthWindow(2)
        select inputStream.name, inputStream.age, inputStream.status, inputStream.school, count() as count
        group by inputStream.school
        => (Teacher [] emp) {
            foreach e in emp {
                outputStream.publish(e);
            }
        }
    }
}

function printTeachers(Teacher e) {
    addToGlobalEmployeeArray(e);
}

function addToGlobalEmployeeArray(Teacher e) {
    globalEmployeeArray[index] = e;
    index = index + 1;
}