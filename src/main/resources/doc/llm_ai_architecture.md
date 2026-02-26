# Large Language Model (LLM) Application Architecture Knowledge

When a user asks to design an LLM app, RAG system, or Agentic workflow, use these reference architectures:

## 1. RAG (Retrieval-Augmented Generation) Architecture

A standard RAG system has two distinct pipelines:

**A. Offline Data Ingestion Pipeline**
- Raw Documents (PDF, Word, Web) -> **Document Loader**
- Loader -> **Text Splitter / Chunker** (splits into paragraphs)
- Chunks -> **Embedding Model** (e.g., text-embedding-ada)
- Embedding Vectors -> **Vector Database** (e.g., Milvus, Pinecone, pgvector)

**B. Online Retrieval & Generation Pipeline**
- User Query -> **Embedding Model**
- Query Embedding -> **Vector Database** (Similarity Search / KNN)
- Vector DB returns **Top-K Context**
- (User Query + Top-K Context) -> **Prompt Template**
- Prompt -> **LLM** (e.g., GPT-4, Claude) -> Final Response to User

## 2. Agentic Workflow Architecture
Agents use LLMs as reasoning engines to call tools recursively.
- **Core Agent Loop (ReAct / Plan & Solve)**: LLM (Brain) loops through: Thought -> Action -> Observation
- **Tools / Plugins**: Search Engine API (Google/Bing), Database Query Tool, API Clients, Code Interpreter
- **Memory Management**: 
  - Short-term Memory (Chat History Window)
  - Long-term Memory (Vector DB retrieval)

## Drawing Guidelines for AI
- **Separation of Concerns**: For RAG diagrams, clearly separate the "Offline Ingestion" pipeline (usually left-to-right) from the "Online Chat" pipeline (top-to-bottom or in parallel).
- **Semantics**: Use cylinders for Vector DBs and standard rounded rectangles for LLMs/Models.
- **Line Clarity**: Ensure the data flow direction is clear with arrows. Avoid bidirectional arrows; prefer sequence loops (A->B, B->C, C->A).