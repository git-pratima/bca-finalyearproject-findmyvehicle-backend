package com.pratima.bca.findmyvehicle.repository.feedback;

import com.pratima.bca.findmyvehicle.entity.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
