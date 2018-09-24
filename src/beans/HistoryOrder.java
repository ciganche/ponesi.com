package beans;

import java.util.List;

public class HistoryOrder {

	String dateTime;
	double cost;
	List<String> orderInfos;
	
	public HistoryOrder()
	{
		
	}
	

	public HistoryOrder(String dateTime, double cost, List<String> orderInfos)
	{
		this.dateTime = dateTime;
		this.cost = cost;
		this.orderInfos = orderInfos;
	}

	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public List<String> getOrderInfos() {
		return orderInfos;
	}
	public void setOrderInfos(List<String> orderInfos) {
		this.orderInfos = orderInfos;
	}
	
	
	
	
}
