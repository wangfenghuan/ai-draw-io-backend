# Entity Relationship (ER) Diagram XML Guide

This guide explains how to structure draw.io XML for Database Schemas and ER Diagrams.

## Core Concepts

ER diagrams use a specific nested structure:
1.  **Table (Entity)**: The main container.
2.  **Row (Attribute)**: Rows inside the table (PK, FK, fields).
3.  **Relationships**: Lines with specific start/end arrows (Crow's Foot notation).

## Style Reference for ER

### 1. Table Container
Use the `swimlane` style to create a table header.
`shape=swimlane;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;html=1;`

### 2. Table Row (Field)
Basic text field inside the stack layout.
`shape=partialRectangle;html=1;whiteSpace=wrap;connectable=0;fillColor=none;top=0;left=0;bottom=0;right=0;overflow=hidden;`

### 3. Crow's Foot Connections (Relationships)
The most critical part is the `endArrow` and `startArrow` styles.

* **One-to-One**: `startArrow=ERmandOne;endArrow=ERmandOne;`
* **One-to-Many**: `startArrow=ERmandOne;endArrow=ERmany;`
* **Zero-to-Many**: `startArrow=ERzeroOne;endArrow=ERzeroMany;`

## XML Structure Example

### Users and Orders (One-to-Many)

```xml
<root>
  <mxCell id="0"/>
  <mxCell id="1" parent="0"/>

  <mxCell id="table_users" value="USERS" style="shape=swimlane;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;html=1;fillColor=#dae8fc;strokeColor=#6c8ebf;" vertex="1" parent="1">
    <mxGeometry x="100" y="100" width="160" height="90" as="geometry"/>
  </mxCell>

  <mxCell id="row_u1" value="PK  user_id" style="shape=partialRectangle;html=1;whiteSpace=wrap;connectable=0;fillColor=none;top=0;left=0;bottom=0;right=0;overflow=hidden;" vertex="1" parent="table_users">
    <mxGeometry y="30" width="160" height="30" as="geometry"/>
  </mxCell>

  <mxCell id="row_u2" value="    email" style="shape=partialRectangle;html=1;whiteSpace=wrap;connectable=0;fillColor=none;top=0;left=0;bottom=0;right=0;overflow=hidden;" vertex="1" parent="table_users">
    <mxGeometry y="60" width="160" height="30" as="geometry"/>
  </mxCell>

  <mxCell id="table_orders" value="ORDERS" style="shape=swimlane;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=1;marginBottom=0;html=1;fillColor=#ffe6cc;strokeColor=#d79b00;" vertex="1" parent="1">
    <mxGeometry x="400" y="100" width="160" height="90" as="geometry"/>
  </mxCell>

  <mxCell id="row_o1" value="PK  order_id" style="shape=partialRectangle;html=1;whiteSpace=wrap;connectable=0;fillColor=none;top=0;left=0;bottom=0;right=0;overflow=hidden;" vertex="1" parent="table_orders">
    <mxGeometry y="30" width="160" height="30" as="geometry"/>
  </mxCell>

  <mxCell id="row_o2" value="FK  user_id" style="shape=partialRectangle;html=1;whiteSpace=wrap;connectable=0;fillColor=none;top=0;left=0;bottom=0;right=0;overflow=hidden;" vertex="1" parent="table_orders">
    <mxGeometry y="60" width="160" height="30" as="geometry"/>
  </mxCell>

  <mxCell id="rel1" value="" style="edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;endArrow=ERmany;startArrow=ERmandOne;" edge="1" parent="1" source="table_users" target="table_orders">
    <mxGeometry relative="1" as="geometry"/>
  </mxCell>
</root>