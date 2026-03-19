import React from 'react';
import { FaEdit, FaTrash } from 'react-icons/fa';

const InstanceWorkoutDay = (props) => {
    const { plan, onEdit, onDelete } = props;

    const formatTime = (timeString) => {
        if (!timeString) return "N/A";
        return timeString;
    };

    return (
                    <div style={{
                minWidth: "280px",
                backgroundColor: "#1a1a2e",
                borderRadius: "12px",
                padding: "16px",
                border: "1px solid #2a2a45",
                color: "#fff"
            }}>
            <div style={{ marginBottom: "8px" }}>
                <h6 style={{ 
                    margin: "0 0 4px 0", 
                    fontSize: "14px", 
                    fontWeight: "600",
                    color: "#fff"
                }}>
                    {plan.title}
                </h6>
                <div style={{
                    fontSize: "12px",
                    color: "#aaa"
                }}>
                    {plan.targetDistance && <span>{plan.targetDistance} km</span>}
                    {plan.targetDistance && plan.duration && <span> • </span>}
                    {plan.duration && <span>{plan.duration}m</span>}
                </div>
            </div>

            {plan.isSetTime && (
                <div style={{
                    fontSize: "12px",
                    color: "#bbb",
                    marginBottom: "8px"
                }}>
                    {plan.startTime && <span>{formatTime(plan.startTime)}</span>}
                    {plan.startTime && plan.endTime && <span> - </span>}
                    {plan.endTime && <span>{formatTime(plan.endTime)}</span>}
                </div>
            )}

            {plan.isCompleted && (
                <div style={{
                    display: "inline-block",
                    color: "#4ade80",
                    fontSize: "11px",
                    fontWeight: "600",
                    marginBottom: "8px"
                }}>
                    ✓ Completed
                </div>
            )}

            <div style={{
                display: "flex",
                gap: "6px",
                justifyContent: "flex-end",
                borderTop: "1px solid #4a4a65",
                paddingTop: "8px",
                marginTop: "8px"
            }}>
                <button 
                    onClick={onEdit}
                    style={{
                        background: "transparent",
                        border: "1px solid #667eea",
                        color: "#667eea",
                        padding: "4px 8px",
                        borderRadius: "4px",
                        cursor: "pointer",
                        fontSize: "12px",
                        display: "flex",
                        alignItems: "center",
                        gap: "4px"
                    }}
                >
                    <FaEdit size={10} /> Edit
                </button>
                <button 
                    onClick={onDelete}
                    style={{
                        background: "transparent",
                        border: "1px solid #f5576c",
                        color: "#f5576c",
                        padding: "4px 8px",
                        borderRadius: "4px",
                        cursor: "pointer",
                        fontSize: "12px",
                        display: "flex",
                        alignItems: "center",
                        gap: "4px"
                    }}
                >
                    <FaTrash size={10} /> Delete
                </button>
            </div>
        </div>
    );
};

export default InstanceWorkoutDay;