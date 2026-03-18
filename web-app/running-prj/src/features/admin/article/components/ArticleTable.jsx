import Table from "react-bootstrap/Table";
import "./ArticleTable.css";

const ArticleTable = (props) => {
    const { articles, loading, handleUpdateArticle, handleDeleteArticle } = props;
    const formatPublishedAt = (publishedAt) => {
        if (!publishedAt) return "";

        const [year, month, day, hour, minute, second] = publishedAt;

        const date = new Date(year, month - 1, day, hour, minute, second);

        return date.toLocaleString();
    };

    return (
        <div>
            <Table striped bordered hover className="article-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Thumbnail</th>
                        <th>Title</th>
                        <th>Category</th>
                        <th>Published At</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>
                    {loading && articles.length === 0 ? (
                        <tr>
                            <td colSpan="6" style={{ textAlign: "center" }}>
                                Loading...
                            </td>
                        </tr>
                    ) : articles.length > 0 ? (
                        articles.map((article) => (
                            <tr key={article.id}>
                                <td>{article.id}</td>

                                <td>
                                    <img
                                        src={article.thumbnailUrl}
                                        alt="thumbnail"
                                        style={{ width: "60px", height: "60px", objectFit: "cover" }}
                                    />
                                </td>

                                <td>{article.title}</td>

                                <td>{article.category}</td>

                                <td>{formatPublishedAt(article.publishedAt)}</td>

                                <td>
                                    <div className="action-buttons">
                                        <i
                                            className="bi bi-pencil-square action-icon icon-edit"
                                            onClick={() => handleUpdateArticle(article)}
                                        ></i>

                                        <i
                                            className="bi bi-trash action-icon icon-delete"
                                            onClick={() => handleDeleteArticle(article)}
                                        ></i>
                                    </div>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="6" style={{ textAlign: "center" }}>
                                No articles
                            </td>
                        </tr>
                    )}
                </tbody>
            </Table>
        </div>
    );
};

export default ArticleTable;