import { Button } from "react-bootstrap";
import { Pencil, ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";

export const PlanActions = ({ onBack, onSave }) => {
    return (
        <div
            style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                marginTop: "30px"
            }}
        >
            {/* Back */}
            <Link
                to={onBack}
                style={{
                    display: "flex",
                    alignItems: "center",
                    gap: "8px",
                    fontSize: "18px",
                    textDecoration: "none",
                    color: "#64748b",
                    fontWeight: "500"
                }}
            >
                <ArrowLeft size={20} />
                Back to Schedule
            </Link>

            {/* Actions */}
            <div style={{ display: "flex", gap: "16px" }}>


                <Button
                    onClick={onSave}
                    style={{
                        background: "#0f172a",
                        border: "none",
                        borderRadius: "999px",
                        padding: "12px 24px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        gap: "10px",
                        fontWeight: "600",
                        fontSize: "16px",
                        boxShadow: "0 6px 20px rgba(0,0,0,0.15)",
                        color: "white"
                    }}
                >
                    💾 Save Plan
                </Button>
            </div>
        </div>
    );
};