# Mind Map XML Guide

This guide explains how to structure draw.io XML for Mind Maps and Brainstorming diagrams.

## Core Concepts

Mind Maps follow a strict tree hierarchy:
1.  **Central Topic**: The root idea (usually an Ellipse or Cloud).
2.  **Branches (Topics)**: Primary categories connected to the center.
3.  **Sub-topics**: Details connected to branches.
4.  **Connections**: Typically curved or straight lines with no arrows (or simple arrows).

## Style Reference for Mind Maps

### 1. Central Topic
`ellipse;whiteSpace=wrap;html=1;align=center;fontStyle=1;fontSize=16;fillColor=#FFCCCC;strokeWidth=3;`

### 2. Main Branch (Level 1)
`rounded=1;whiteSpace=wrap;html=1;arcSize=50;fillColor=#FFF2CC;strokeColor=#D6B656;fontSize=14;`

### 3. Sub Branch (Level 2)
`text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;whiteSpace=wrap;rounded=0;fontSize=12;` (Often just text without a box, or a simple underline)

### 4. Connections
Mind maps often use curved lines.
`edgeStyle=entityRelationEdgeStyle;fontSize=12;html=1;endArrow=none;curved=1;strokeWidth=2;`

## XML Structure Example

### Project Planning Mind Map

```xml
<root>
<mxCell id="0"/>
<mxCell id="1" parent="0"/>

<mxCell id="center" value="Project X" style="ellipse;whiteSpace=wrap;html=1;align=center;fontStyle=1;fontSize=16;fillColor=#dae8fc;strokeColor=#6c8ebf;strokeWidth=3;" vertex="1" parent="1">
<mxGeometry x="350" y="250" width="120" height="80" as="geometry"/>
</mxCell>

<mxCell id="b1" value="Development" style="rounded=1;whiteSpace=wrap;html=1;arcSize=50;fillColor=#d5e8d4;strokeColor=#82b366;" vertex="1" parent="1">
<mxGeometry x="550" y="150" width="100" height="40" as="geometry"/>
</mxCell>

<mxCell id="e1" style="edgeStyle=entityRelationEdgeStyle;endArrow=none;curved=1;strokeWidth=2;" edge="1" parent="1" source="center" target="b1">
<mxGeometry relative="1" as="geometry"/>
</mxCell>

<mxCell id="b1_1" value="Frontend" style="text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;whiteSpace=wrap;rounded=0;" vertex="1" parent="1">
<mxGeometry x="700" y="130" width="80" height="30" as="geometry"/>
</mxCell>

<mxCell id="e1_1" style="edgeStyle=orthogonalEdgeStyle;rounded=1;endArrow=none;" edge="1" parent="1" source="b1" target="b1_1">
<mxGeometry relative="1" as="geometry"/>
</mxCell>

<mxCell id="b2" value="Marketing" style="rounded=1;whiteSpace=wrap;html=1;arcSize=50;fillColor=#ffe6cc;strokeColor=#d79b00;" vertex="1" parent="1">
<mxGeometry x="150" y="150" width="100" height="40" as="geometry"/>
</mxCell>

<mxCell id="e2" style="edgeStyle=entityRelationEdgeStyle;endArrow=none;curved=1;strokeWidth=2;" edge="1" parent="1" source="center" target="b2">
<mxGeometry relative="1" as="geometry"/>
</mxCell>
</root>