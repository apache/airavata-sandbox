#!/usr/bin/env cwl-runner

cwlVersion: v1.0
class: Workflow
inputs:
  test_file: string
  arg_file1: string
  arg_file2: string

outputs: []

steps:
  job1:
    run: script_runner.cwl
    in:
      test_script: test_file
      arg_file: arg_file1
    out: []

  job2:
    run: script_runner.cwl
    in:
      test_script: test_file
      arg_file: arg_file2
    out: []