# AI Application / Agent Architecture Knowledge Base

When a user asks to architect an AI application, RAG system, LLM integration, Spring AI, LangChain4j, or an Agentic workflow, you MUST consult this knowledge base to design a professional and standard architecture.

## Core Concepts & Standard Components

1.  **AI Gateway / Orchestrator**: The entry point for AI requests. often handles authentication, rate limiting, and routing to different LLM providers or specialized agents.
2.  **RAG (Retrieval-Augmented Generation) Pipeline**:
    *   **Document Loaders & Parsers**: Extracting text from PDFs, URLs, databases.
    *   **Text Splitters / Chunking**: Breaking documents into semantic chunks.
    *   **Embedding Model**: Converting chunks into vector representations (e.g., OpenAI text-embedding-ada-002, BAAI/bge).
    *   **Vector Database**: Storing and searching embeddings (e.g., Milvus, ChromaDB, Pinecone, pgvector, Redis, Elasticsearch).
3.  **LLM Providers / Models**: The core reasoning engine (OpenAI GPT-4, Anthropic Claude, Google Gemini, Local Llama 3 via Ollama/vLLM).
4.  **Frameworks**:
    *   **Spring AI**: Java-based framework integrating AI models, Vector DBs, and Prompts seamlessly into Spring Boot.
    *   **LangChain / LangChain4j**: Frameworks for building context-aware reasoning applications, heavily using Chains and Tools.
5.  **Agentic Architecture**:
    *   **Router Agent**: Determines user intent and routes to the correct sub-agent.
    *   **Tools / Function Calling**: External APIs the agent can invoke (e.g., Web Search, SQL Query, Calculator, internal APIs).
    *   **Memory / Context**: Short-term (Conversation History/Redis) and Long-term (Vector DB/User Knowledge).
    *   **Planner / Critic**: Agents that break down tasks and evaluate the results before responding.

## Common Architecture Patterns

### 1. Standard RAG (Retrieval-Augmented Generation)
*   **Offline Data Ingestion**: Raw Data -> Document Parser -> Chunker -> Embedding Model -> Vector DB.
*   **Online Query**: User Query -> Prompt Engineering (Rewrite) -> Embedding Model -> Vector DB Search -> Retrieve Top-K Context -> LLM Generation -> Final Response.
*   **Drawing Tip**: Use a vertical swimlane for Ingestion and another for Querying.

### 2. Multi-Agent System (Agentic Workflow)
*   User -> Orchestrator Agent.
*   Orchestrator -> Tool A (Search Agent), Tool B (Code Agent), Tool C (Data Analysis Agent).
*   Agents loop through observation, thought, and action.
*   **Drawing Tip**: Show bidirectional flow between the LLM Reasoning Engine and the Tools/Memory. Use distinct colors for different Agent Personas.

## Drawing Guidelines for AI

*   **Colors**:
    *   **Vector Databases**: Use Green `fillColor=#E8F5E9;` to signify storage.
    *   **LLM Models (GPT/Claude)**: Use Purple `fillColor=#F3E5F5;` or magical colors.
    *   **Tools/Functions**: Use Orange `fillColor=#FFF3E0;`.
    *   **Orchestration/Code (Spring AI/LangChain4j)**: Use Blue `fillColor=#E1E8EE;`.
*   **Icons (if applicable via shapes)**: Use cylinder shapes `shape=cylinder3` for Vector DBs. Use standard rounded rectangles for Chains and Prompts.
*   **Groups**: Enclose the entire RAG pipeline in a `swimlane` named "RAG Pipeline". Enclose multiple specialized agents in a "Multi-Agent Hub" swimlane.
*   **Data Flow**: Clearly show the "Retrieval" arrows coming OUT of the Vector DB and going INTO the LLM Prompt construction bounding box.
