CREATE TABLE `shapes` (
    shape_id VARCHAR(20),
    shape_pt_lat DECIMAL(8,6),
    shape_pt_lon DECIMAL(8,6),
    shape_pt_sequence INT(11),
    shape_dist_traveled DECIMAL(8,4),
    KEY `shape_id` (shape_id),
    KEY `shape_pt_sequence` (shape_pt_sequence)
);