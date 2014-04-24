package org.cloudfoundry.community.servicebroker.model.fixture;

import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.community.servicebroker.model.Plan;

public class PlanFixture {

	public static List<Plan> getAllPlans() {
		List<Plan> plans = new ArrayList<Plan>();
		plans.add(getPlanOne());
		plans.add(getPlanTwo());
		return plans;
	}
		
	public static Plan getPlanOne() {
		return new Plan("plan-one-id", "Plan One", "Description for Plan One");
	}
	
	public static Plan getPlanTwo() {
		return new Plan("plan-two-id", "Plan Two", "Description for Plan Two");
	}
	
}
