import React from "react";
import { Compass, SearchX, UsersRound, WifiOff } from "lucide-react";

const EMPTY_CONFIG = {
    error: { icon: WifiOff, title: "Không thể tải danh sách follow", description: "Có lỗi xảy ra khi lấy dữ liệu. Hãy thử tải lại để tiếp tục." },
    noResult: { icon: SearchX, title: "Không tìm thấy ai phù hợp", description: "Thử từ khóa ngắn hơn hoặc tăng bán kính để mở rộng kết quả." },
    following: { icon: UsersRound, title: "Bạn chưa follow ai", description: "Khám phá runner gần bạn và bắt đầu follow để xây dựng network." },
    followers: { icon: UsersRound, title: "Chưa có follower nào", description: "Hoàn thiện hồ sơ và hoạt động nhiều hơn để thu hút follower." },
    discover: { icon: Compass, title: "Hiện chưa có gợi ý phù hợp", description: "Hãy đổi khoảng cách hoặc thử lại với bộ lọc khác." },
};

const FollowEmptyState = ({ type, onRetry }) => {
    const config = EMPTY_CONFIG[type] || EMPTY_CONFIG.discover;
    const Icon = config.icon;

    return (
        <div className="follow-empty">
            <div className="follow-empty__icon"><Icon size={28} /></div>
            <h3>{config.title}</h3>
            <p>{config.description}</p>
            {type === "error" && (
                <button type="button" className="follow-secondary-btn" onClick={onRetry}>
                    Thử lại
                </button>
            )}
        </div>
    );
};

export default FollowEmptyState;
