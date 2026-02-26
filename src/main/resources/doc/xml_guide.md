# Draw.io XML Syntax & Best Practices

> **CRITICAL**: You only need to generate `mxCell` elements. Wrapper tags (`mxfile`, `mxGraphModel`, `root`) and root cells (id="0", id="1") are added automatically.

## Core Syntax Rules
1. **Never Nest**: All `mxCell` are siblings. Do not put an `mxCell` inside another (exception for grouped elements visually, but XML-wise they remain siblings with parent references).
2. **IDs & Parents**: IDs start from `"2"`. Every cell needs a unique `id` and a `parent` attribute. `parent="1"` is for top-level shapes. `parent="<container-id>"` is for elements inside a group or swimlane.
3. **Text Nodes**: Text cells **must** include `whiteSpace=wrap;html=1;` in style.
4. **Line Breaks**: Use `&lt;br&gt;` for newlines. Never use `\n`.
5. **XML Constraints**: **No XML comments** (`<!-- -->`). They break downstream ID matching.

## Shape Geometry (CRITICAL Constraint)
To avoid overlapping nodes (occlusion), you MUST calculate realistic `x` and `y` coordinates for every shape BEFORE outputting it.
- Space standard shapes by at least **150px horizontally** and **100px vertically**.
- Typical shape dimensions: `width="120" height="60"`.
- Container/Swimlane dimensions: `width="400" height="300"`.
- If an architecture diagram has 3 vertical layers, their `y` coordinates should roughly be: `y=100` (Layer 1), `y=300` (Layer 2), `y=500` (Layer 3).

## Edge Routing & Ports (CRITICAL Constraint)
Never draw diagonal intersecting lines causing a chaotic web.
- Always use modern routing over simplistic lines: `edgeStyle=orthogonalEdgeStyle;rounded=1;endArrow=blockThin;endFill=1;`
- **Port Explicitly**: Always add `exitX, exitY, entryX, entryY` constraints!
  - `exitX=1;exitY=0.5;entryX=0;entryY=0.5;` is perfect for a strict left-to-right connection.
  - `exitX=0.5;exitY=1;entryX=0.5;entryY=0;` is perfect for a strict top-to-bottom connection.
- **Waypoints**: Use `<Array as="points">` with `<mxPoint x=".." y=".."/>` to route an edge completely around an obstacle shape to prevent the line from crossing through the shape.

## Beautiful Aesthetics & Colors
Use pastel and modern flat colors to make architecture diagrams pop!
- **Base Shape Options**: `rounded=1;shadow=0;glass=0;sketch=0;`
- **Blue (Business Services)**: `fillColor=#DAE8FC;strokeColor=#6c8ebf;`
- **Green (Databases / Storage)**: `fillColor=#D5E8D4;strokeColor=#82b366;`
- **Orange (MQ / Cache / Async)**: `fillColor=#FFE6CC;strokeColor=#d79b00;`
- **Purple (Gateway / Proxies)**: `fillColor=#E1D5E7;strokeColor=#9673a6;`
- **Groups / VPCs (Containers)**: `fillColor=#F5F5F5;strokeColor=#B3B3B3;dashed=1;`
