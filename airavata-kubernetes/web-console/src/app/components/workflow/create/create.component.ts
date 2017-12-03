import {Component, ViewEncapsulation, AfterViewInit} from "@angular/core";
import {TaskService} from "../../../services/task.service";
import {TaskType} from "../../../models/task/type/task.type.model";
import {OperationType} from "../../../models/task/type/operation/operation.type.model";
import {OperationInPort} from "../../../models/task/type/operation/operation.inport.type.model";
import {OperationOutPort} from "../../../models/task/type/operation/operation.outport.type.model";
import {WorkflowService} from "../../../services/workflow.service";
import {Router} from "@angular/router";

declare let mxClient: any;
declare let mxUtils: any;
declare let mxToolbar: any;
declare let mxDivResizer: any;
declare let mxGraph: any;
declare let mxDragSource: any;
declare let mxRubberband: any;
declare let mxKeyHandler: any;
declare let mxCodec: any;
declare let mxConstants: any;
declare let mxEdgeStyle: any;
declare let mxEvent: any;
declare let mxForm: any;
declare let mxCellAttributeChange: any;
declare let mxRectangle: any;
declare let mxPoint: any;

@Component({
  templateUrl: './create.html',
  encapsulation: ViewEncapsulation.None,
  providers: [TaskService, WorkflowService]
})
export class WorkflowCreateComponent implements AfterViewInit {

  taskTypes: Array<TaskType> = [];
  operationTypes: Array<OperationType> = [];
  graphInstance: any;
  workFlowName: string = "Sample Workflow";

  constructor(private taskService: TaskService, private workflowService: WorkflowService, private router: Router) {

    let startType: OperationType = new OperationType();
    startType.id = 1;
    startType.name = "Start";
    startType.icon = "assets/icons/start.png";
    startType.outPorts.push(new OperationOutPort(1, "Output"));

    let endType: OperationType = new OperationType();
    endType.id = 1;
    endType.name = "Stop";
    endType.icon = "assets/icons/stop.png";
    endType.inPorts.push(new OperationInPort(1, "Input"));

    this.operationTypes.push(startType, endType);
  }

  loadGraphData() {
    this.taskService.getAllTaskTypes().subscribe(data => {
      this.taskTypes = data;
      this.loadGraph(document.getElementById('graphContainer'), document.getElementById('toolContainer'));
    }, err => {

    })
  }

  onShowWorkflowXMLClicked() {
    let encoder = new mxCodec();
    let node = encoder.encode(this.graphInstance.getModel());
    mxUtils.popup(mxUtils.getPrettyXml(node), true);
  }

  onCreateWorkflowClicked() {
    let encoder = new mxCodec();
    let node = encoder.encode(this.graphInstance.getModel());
    this.workflowService.createWorkflow(this.workFlowName, mxUtils.getPrettyXml(node)).subscribe(data => {
      alert("Workflow created");
      this.routeToListPage();
    }, err => {
      alert("An error occurred");
    });
  }

  routeToListPage() {
    this.router.navigateByUrl("/workflow");
  }

  ngAfterViewInit(): void {
    this.loadGraphData();
  }

  loadGraph(container, tbContainer) {

    if (!mxClient.isBrowserSupported()) {
      // Displays an error message if the browser is not supported.
      mxUtils.error('Browser is not supported!', 200, false);
    } else {
      let toolbar = new mxToolbar(tbContainer);
      toolbar.enabled = false;

      // Workaround for Internet Explorer ignoring certain styles
      if (mxClient.IS_QUIRKS) {
        document.body.style.overflow = 'hidden';
        new mxDivResizer(tbContainer);
        new mxDivResizer(container);
      }

      let doc = mxUtils.createXmlDocument();

      let relation = doc.createElement('Edge');

      let graph = new mxGraph(container);
      this.graphInstance = graph;

      graph.dropEnabled = true;

      mxDragSource.prototype.getDropTarget = function (graph, x, y) {
        let cell = graph.getCellAt(x, y);

        if (!graph.isValidDropTarget(cell)) {
          cell = null;
        }

        return cell;
      };

      // Enables new connections in the graph
      graph.setConnectable(true);
      graph.setMultigraph(false);

      let addTaskType = function (icon, w, h, taskType: TaskType) {
        addToolbarItem(graph, toolbar, createTaskItem(doc, taskType), icon, doc);
      };

      let addOperation = function (icon, w, h, operationType: OperationType) {
        addToolbarItem(graph, toolbar, createOperation(doc, operationType), icon, doc);
      };

      this.taskTypes.forEach((taskType) => {
        addTaskType(taskType.icon, 40, 40, taskType);
      });

      this.operationTypes.forEach((operationType) => {
        addOperation(operationType.icon, 40, 40, operationType);
      });

      toolbar.addLine();

      graph.setCellsResizable(false);
      graph.setResizeContainer(true);
      graph.minimumContainerSize = new mxRectangle(0, 0, 500, 380);
      graph.setBorder(60);

      new mxKeyHandler(graph);

      // Overrides method to disallow edge label editing
      graph.isCellEditable = function (cell) {
        return !this.getModel().isEdge(cell);
      };

      // Overrides method to provide a cell label in the display
      graph.convertValueToString = function (cell) {
        if (mxUtils.isNode(cell.value)) {
          if (cell.value.nodeName.toLowerCase() == 'processingelement') {
            return cell.getAttribute('name', '');
          }
          if (cell.value.nodeName.toLowerCase() == 'operation') {
            return cell.getAttribute('name', '');
          }
          else if (cell.value.nodeName.toLowerCase() == 'edge') {
            return '';
          }

        }
        return '';
      };

      // Overrides method to store a cell label in the model
      let cellLabelChanged = graph.cellLabelChanged;
      graph.cellLabelChanged = function (cell, newValue, autoSize) {
        if (mxUtils.isNode(cell.value) &&
          cell.value.nodeName.toLowerCase() == 'processingelement') {
          // Clones the value for correct undo/redo
          let elt = cell.value.cloneNode(true);

          elt.setAttribute('name', newValue);
          newValue = elt;
          autoSize = true;
        }

        cellLabelChanged.apply(this, arguments);
      };

      // Overrides method to create the editing value
      let getEditingValue = graph.getEditingValue;
      graph.getEditingValue = function (cell) {
        if (mxUtils.isNode(cell.value) &&
          cell.value.nodeName.toLowerCase() == 'processingelement') {
          return cell.getAttribute('name', '');
        }
      };

      new mxRubberband(graph);

      // Changes the style for match the markup
      // Creates the default style for vertices
      let style = graph.getStylesheet().getDefaultVertexStyle();
      style[mxConstants.STYLE_STROKECOLOR] = 'gray';
      style[mxConstants.STYLE_ROUNDED] = true;
      style[mxConstants.STYLE_SHADOW] = true;
      style[mxConstants.STYLE_FILLCOLOR] = '#DFDFDF';
      style[mxConstants.STYLE_GRADIENTCOLOR] = 'white';
      style[mxConstants.STYLE_FONTCOLOR] = 'black';
      style[mxConstants.STYLE_FONTSIZE] = '12';
      style[mxConstants.STYLE_SPACING] = 4;

      // Creates the default style for edges
      style = graph.getStylesheet().getDefaultEdgeStyle();
      style[mxConstants.STYLE_STROKECOLOR] = '#0C0C0C';
      style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = 'white';
      style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;
      style[mxConstants.STYLE_ROUNDED] = true;
      style[mxConstants.STYLE_FONTCOLOR] = 'black';
      style[mxConstants.STYLE_FONTSIZE] = '10';

      let parent = graph.getDefaultParent();
      graph.getModel().beginUpdate();
      try {
        //var v1 = graph.insertVertex(parent, null, pe1, 40, 40, 80, 30);
        //var v2 = graph.insertVertex(parent, null, pe2, 200, 150, 80, 30);
        //var e1 = graph.insertEdge(parent, null, relation, v1, v2);
      } finally {
        // Updates the display
        graph.getModel().endUpdate();
      }

      // Implements a properties panel that uses
      // mxCellAttributeChange to change properties
      graph.getSelectionModel().addListener(mxEvent.CHANGE, function (sender, evt) {
        selectionChanged(graph);
      });

      selectionChanged(graph);

    }

    /**
     * Updates the properties panel
     */
    function selectionChanged(graph) {
      let div = document.getElementById('properties');

      // Forces focusout in IE
      graph.container.focus();

      // Clears the DIV the non-DOM way
      div.innerHTML = '';

      // Gets the selection cell
      let cell = graph.getSelectionCell();

      if (cell == null) {
        mxUtils.writeln(div, 'Nothing selected.');
      }
      else if (cell.value) {
        // Writes the title
        let center = document.createElement('center');
        mxUtils.writeln(center, cell.value.nodeName + ' (' + cell.id + ')');
        div.appendChild(center);
        mxUtils.br(div);

        // Creates the form from the attributes of the user object
        let form = new mxForm();

        let attrs = cell.value.attributes;

        for (let i = 0; i < attrs.length; i++) {
          if (!attrs[i].nodeName.startsWith("in-") && !attrs[i].nodeName.startsWith("out-")) {
            createTextField(graph, form, cell, attrs[i]);
          }
        }

        div.appendChild(form.getTable());
        mxUtils.br(div);
      }
    }

    function createTaskItem(doc, taskType: TaskType) {

      var pe = doc.createElement('ProcessingElement');
      pe.setAttribute('name', taskType.name);
      pe.setAttribute('Type', taskType.id);

      taskType.inputTypes.forEach((input, index) => {
        pe.setAttribute(input.name, input.defaultValue);
      });

      taskType.outputTypes.forEach((output, index) => {
        pe.setAttribute('output-' + output.name, '');
      });

      taskType.outPorts.forEach((outPort, index) => {
        pe.setAttribute("out-" + outPort.id, outPort.name);
      });

      pe.setAttribute("in-1" , "Input");

      return pe;
    }

    function createOperation(doc, operation: OperationType) {
      var op = doc.createElement('Operation');
      op.setAttribute("name", operation.name);
      operation.outPorts.forEach((outPort, index) => {
        op.setAttribute("out-" + outPort.id, outPort.name);
      });
      operation.inPorts.forEach((inPort, index) => {
        op.setAttribute("in-" + inPort.id, inPort.name);
      });
      return op;
    }

    function createTextField(graph, form, cell, attribute) {
      let input = form.addText(attribute.nodeName + ':', attribute.nodeValue);

      let applyHandler = function () {
        let newValue = input.value || '';
        let oldValue = cell.getAttribute(attribute.nodeName, '');

        if (newValue != oldValue) {
          graph.getModel().beginUpdate();

          try {
            let edit = new mxCellAttributeChange(
              cell, attribute.nodeName,
              newValue);
            graph.getModel().execute(edit);
            graph.updateCellSize(cell);
          }
          finally {
            graph.getModel().endUpdate();
          }
        }
      };

      mxEvent.addListener(input, 'keypress', function (evt) {
        // Needs to take shift into account for textareas
        if (evt.keyCode == /*enter*/13 && !mxEvent.isShiftDown(evt)) {
          input.blur();
        }
      });

      if (mxClient.IS_IE) {
        mxEvent.addListener(input, 'focusout', applyHandler);
      }
      else {
        // Note: Known problem is the blurring of fields in
        // Firefox by changing the selection, in which case
        // no event is fired in FF and the change is lost.
        // As a workaround you should use a local variable
        // that stores the focused field and invoke blur
        // explicitely where we do the graph.focus above.
        mxEvent.addListener(input, 'blur', applyHandler);
      }
    }

    function addToolbarItem(graph, toolbar, prototype, image, doc) {
      // Function that is executed when the image is dropped on
      // the graph. The cell argument points to the cell under
      // the mousepointer if there is one.
      let funct = function (graph, evt, cell, x, y) {

        let parent = graph.getDefaultParent();
        let model = graph.getModel();

        let v1 = null;

        model.beginUpdate();

        try {

          let inputs = [];
          let inputKeys = [];
          let outputs = [];
          let outputKeys = [];
          for (let i = 0; i < prototype.attributes.length; i++) {
            let attr = prototype.attributes[i];
            if (attr.nodeName.startsWith("in-")) {
              inputs.push(attr.nodeValue);
              inputKeys.push(attr.nodeName)
            }
            if (attr.nodeName.startsWith("out-")) {
              outputs.push(attr.nodeValue);
              outputKeys.push(attr.nodeName);
            }
          }

          v1 = graph.insertVertex(parent, null, prototype.cloneNode(true), x, y, 80, outputs.length*20 + 20);
          v1.setConnectable(false);

          let inputDivision = 1 / (inputs.length + 1);
          let outputDivision = 1 / (outputs.length + 1);

          inputs.forEach(function (input, index) {
            let port = doc.createElement('InPort');
            port.setAttribute('name', input);
            port.setAttribute("port-id", inputKeys[index]);

            let v11 = graph.insertVertex(v1, null, port, 0, (index * inputDivision + inputDivision), 10, 10);
            v11.geometry.offset = new mxPoint(-5, -5);
            v11.geometry.relative = true;
          });

          outputs.forEach(function (output, index) {
            let port = doc.createElement('OutPort');
            port.setAttribute('name', output);
            port.setAttribute("port-id", outputKeys[index]);

            let v11 = graph.insertVertex(v1, null, port, 1, (index * outputDivision + outputDivision), 10, 10);
            v11.geometry.offset = new mxPoint(-5, -5);
            v11.geometry.relative = true;
          });
        }
        finally {
          // Updates the display
          graph.getModel().endUpdate();
        }

        graph.updateCellSize(v1);
        graph.setSelectionCell(v1);
      };

      // Creates the image which is used as the drag icon (preview)
      let img = toolbar.addMode(null, image, funct);
      mxUtils.makeDraggable(img, graph, funct);
    }
  };
}
