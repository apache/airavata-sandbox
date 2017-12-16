#!/usr/bin/env cwl-runner

cwlVersion: v1.0
class: CommandLineTool
baseCommand: python3.6
inputs:
  test_script:
    type: string
    inputBinding:
      position: 1
  arg_file:
    type: string
    inputBinding:
      position: 2
outputs: []