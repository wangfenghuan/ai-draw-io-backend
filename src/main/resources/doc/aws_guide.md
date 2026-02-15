# AWS Architecture Diagram XML Guide

This guide explains how to structure draw.io XML for AWS Cloud Architecture diagrams.

## Core Concepts

AWS diagrams rely heavily on:
1.  **Containers**: Representing Regions, VPCs, Availability Zones, and Subnets.
2.  **Resources**: Specific services like EC2, RDS, Lambda (often using specific shapes or icons).
3.  **Groups**: Nesting resources inside subnets/VPCs using `parent` attributes.

## Style Reference for AWS

### 1. Containers (VPC, Subnet, Region)
Containers are usually rounded rectangles with dashed or solid colored borders and transparent backgrounds.

**VPC Style:**
`group;container=1;collapsible=1;rounded=1;arcSize=10;dashed=1;strokeColor=#8C4FFF;fillColor=none;verticalAlign=top;align=left;spacingTop=10;fontColor=#8C4FFF;fontStyle=1;whiteSpace=wrap;html=1;`

**Public Subnet Style:**
`group;container=1;collapsible=1;rounded=1;arcSize=10;dashed=1;strokeColor=#7AA116;fillColor=#F2F8E6;verticalAlign=top;align=left;spacingTop=10;fontColor=#7AA116;whiteSpace=wrap;html=1;`

**Private Subnet Style:**
`group;container=1;collapsible=1;rounded=1;arcSize=10;dashed=1;strokeColor=#0073BB;fillColor=#E6F2F8;verticalAlign=top;align=left;spacingTop=10;fontColor=#0073BB;whiteSpace=wrap;html=1;`

### 2. Resource Icons (EC2, S3, RDS)
While draw.io has a specific AWS shape library (`mxgraph.aws4...`), it is complex to generate. A robust alternative is to use standard shapes with AWS colors or imported images.

**Standard Generic Resource (Orange - Compute):**
`rounded=1;whiteSpace=wrap;html=1;fillColor=#FF9900;strokeColor=none;fontColor=#ffffff;fontStyle=1;`

**Standard Generic Resource (Blue - Database):**
`shape=cylinder3;whiteSpace=wrap;html=1;boundedLbl=1;backgroundOutline=1;size=15;fillColor=#3B48CC;strokeColor=none;fontColor=#ffffff;`

## XML Structure Example

### VPC with Public Subnet and EC2 Instance

```xml
<root>
  <mxCell id="0"/>
  <mxCell id="1" parent="0"/>

  <mxCell id="vpc1" value="VPC (10.0.0.0/16)" style="group;rounded=1;dashed=1;strokeColor=#8C4FFF;fillColor=none;verticalAlign=top;align=left;spacingTop=5;" vertex="1" parent="1">
    <mxGeometry x="40" y="40" width="400" height="300" as="geometry"/>
  </mxCell>

  <mxCell id="subnet1" value="Public Subnet" style="group;rounded=1;dashed=1;strokeColor=#7AA116;fillColor=#F2F8E6;verticalAlign=top;align=left;spacingTop=5;" vertex="1" parent="vpc1">
    <mxGeometry x="20" y="40" width="360" height="120" as="geometry"/>
  </mxCell>

  <mxCell id="ec2_1" value="Web Server" style="rounded=1;fillColor=#FF9900;strokeColor=none;fontColor=#ffffff;" vertex="1" parent="subnet1">
    <mxGeometry x="40" y="40" width="80" height="40" as="geometry"/>
  </mxCell>

  <mxCell id="rds1" value="Primary DB" style="shape=cylinder3;fillColor=#3B48CC;strokeColor=none;fontColor=#ffffff;" vertex="1" parent="vpc1">
    <mxGeometry x="60" y="200" width="60" height="60" as="geometry"/>
  </mxCell>

  <mxCell id="edge1" value="SQL (3306)" style="edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;dashed=1;" edge="1" parent="1" source="ec2_1" target="rds1">
    <mxGeometry relative="1" as="geometry"/>
  </mxCell>
</root>