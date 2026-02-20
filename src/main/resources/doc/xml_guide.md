# Draw.io XML Quick Reference

> **NOTE:** You only need to generate `mxCell` elements. Wrapper tags (`mxfile`, `mxGraphModel`, `root`) and root cells (id="0", id="1") are added automatically.

## mxCell — Shape (vertex)
```xml
<mxCell id="2" value="Label" style="rounded=1;whiteSpace=wrap;html=1;" vertex="1" parent="1">
  <mxGeometry x="100" y="100" width="120" height="60" as="geometry"/>
</mxCell>
```

## mxCell — Connector (edge)
```xml
<mxCell id="3" style="edgeStyle=orthogonalEdgeStyle;exitX=1;exitY=0.5;entryX=0;entryY=0.5;endArrow=classic;html=1;" edge="1" parent="1" source="2" target="4">
  <mxGeometry relative="1" as="geometry"/>
</mxCell>
```

## Key Rules
1. All `mxCell` are siblings — **never nest** one inside another.
2. IDs start from `"2"`. Every cell needs a unique `id` and a `parent` attribute.
3. `parent="1"` for top-level shapes; `parent="<container-id>"` for grouped/swimlane children.
4. Text cells **must** include `whiteSpace=wrap;html=1;` in style.
5. Line breaks: use `&lt;br&gt;` (e.g. `value="Line1&lt;br&gt;Line2"`). Never use `\n`.
6. Escape in values: `&lt;` `&gt;` `&amp;` `&quot;`.
7. **No XML comments** (`<!-- -->`). They break edit_diagram ID matching.

## Common Shapes
| Shape | Style key |
|-------|-----------|
| Rectangle | `rounded=0;` or `rounded=1;` |
| Ellipse/Circle | `ellipse;` |
| Diamond | `rhombus;` |
| Cylinder | `shape=cylinder;` |
| Cloud | `shape=cloud;` |
| Swimlane | `swimlane;startSize=30;` |

## Edge Styles
```
endArrow=classic|block|open|none
startArrow=none|classic
edgeStyle=orthogonalEdgeStyle|elbowEdgeStyle|entityRelationEdgeStyle
curved=1
```

## Swimlane Pattern
```xml
<mxCell id="lane1" value="Frontend" style="swimlane;startSize=30;" vertex="1" parent="1">
  <mxGeometry x="40" y="40" width="200" height="300" as="geometry"/>
</mxCell>
<!-- Step inside lane (parent = lane id) -->
<mxCell id="s1" value="Step 1" style="rounded=1;whiteSpace=wrap;html=1;" vertex="1" parent="lane1">
  <mxGeometry x="20" y="60" width="160" height="40" as="geometry"/>
</mxCell>
<!-- Edge is a sibling under root (parent="1"), NOT inside the lane -->
<mxCell id="e1" style="edgeStyle=orthogonalEdgeStyle;endArrow=classic;html=1;" edge="1" parent="1" source="s1" target="s2">
  <mxGeometry relative="1" as="geometry"/>
</mxCell>
```

## Edge with Waypoints (obstacle avoidance)
```xml
<mxCell id="e2" style="edgeStyle=orthogonalEdgeStyle;exitX=0.5;exitY=1;entryX=0.5;entryY=0;endArrow=classic;html=1;" edge="1" parent="1" source="a" target="b">
  <mxGeometry relative="1" as="geometry">
    <Array as="points">
      <mxPoint x="300" y="200"/>
    </Array>
  </mxGeometry>
</mxCell>
```

## Group / Container
```xml
<mxCell id="10" value="Group" style="group;" vertex="1" connectable="0" parent="1">
  <mxGeometry x="200" y="200" width="200" height="100" as="geometry"/>
</mxCell>
<mxCell id="11" value="Child" style="rounded=0;whiteSpace=wrap;html=1;" vertex="1" parent="10">
  <mxGeometry width="90" height="40" as="geometry"/>
</mxCell>
```
