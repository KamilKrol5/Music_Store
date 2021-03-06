package model.entities;

import model.Payment;
import model.Status;
import model.TransactionDocument;

import javax.persistence.*;

@Entity
@Table(name = "orders", schema = "music_store")
public class OrdersEntity {
    private int orderId;
//    private int deliveryId;
    private String customerName;
    private String customerAddress;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Status currentStatus;
    @Enumerated(EnumType.STRING)
    private Payment payment;
    @Enumerated(EnumType.STRING)
    private TransactionDocument transactionDocument;


    @ManyToOne
    @JoinColumn(name = "delivery_id")
    public DeliveryEntity getDeliveryEntity() {
        return deliveryEntity;
    }

    public void setDeliveryEntity(DeliveryEntity deliveryEntity) {
        this.deliveryEntity = deliveryEntity;
    }

    private DeliveryEntity deliveryEntity;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

//    @Basic
//    @Column(name = "delivery_id")
//    public int getDeliveryId() {
//        return deliveryId;
//    }
//
//    public void setDeliveryId(int deliveryId) {
//        this.deliveryId = deliveryId;
//    }

    @Basic
    @Column(name = "customer_name")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Basic
    @Column(name = "customer_address")
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "current_status")
    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Basic
    @Column(name = "payment")
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Basic
    @Column(name = "transaction_document")
    public TransactionDocument getTransactionDocument() {
        return transactionDocument;
    }

    public void setTransactionDocument(TransactionDocument transactionDocument) {
        this.transactionDocument = transactionDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (orderId != that.orderId) return false;
        if (customerName != null ? !customerName.equals(that.customerName) : that.customerName != null) return false;
        if (customerAddress != null ? !customerAddress.equals(that.customerAddress) : that.customerAddress != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (currentStatus != null ? !currentStatus.equals(that.currentStatus) : that.currentStatus != null)
            return false;
        if (payment != null ? !payment.equals(that.payment) : that.payment != null) return false;
        return transactionDocument != null ? transactionDocument.equals(that.transactionDocument) : that.transactionDocument == null;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + (customerName != null ? customerName.hashCode() : 0);
        result = 31 * result + (customerAddress != null ? customerAddress.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (currentStatus != null ? currentStatus.hashCode() : 0);
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        result = 31 * result + (transactionDocument != null ? transactionDocument.hashCode() : 0);
        return result;
    }
}
