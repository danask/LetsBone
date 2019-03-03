/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\douglas\\Mobile Development 2\\AIDLClientWithParcelableObject\\AIDLClient\\app\\src\\main\\aidl\\com\\example\\wongi5\\aidlserver\\ICalService.aidl
 */
package com.example.wongi5.aidlserver;
// Declare any non-default types here with import statements

public interface ICalService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.wongi5.aidlserver.ICalService
{
private static final java.lang.String DESCRIPTOR = "com.example.wongi5.aidlserver.ICalService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.wongi5.aidlserver.ICalService interface,
 * generating a proxy if needed.
 */
public static com.example.wongi5.aidlserver.ICalService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.wongi5.aidlserver.ICalService))) {
return ((com.example.wongi5.aidlserver.ICalService)iin);
}
return new com.example.wongi5.aidlserver.ICalService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_getMessage:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getMessage(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getResult:
{
data.enforceInterface(descriptor);
double _result = this.getResult();
reply.writeNoException();
reply.writeDouble(_result);
return true;
}
case TRANSACTION_setOperant:
{
data.enforceInterface(descriptor);
com.example.wongi5.aidlserver.Multiplication _arg0;
if ((0!=data.readInt())) {
_arg0 = com.example.wongi5.aidlserver.Multiplication.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.setOperant(_arg0);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.example.wongi5.aidlserver.ICalService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getMessage(java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_getMessage, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public double getResult() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
double _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getResult, _data, _reply, 0);
_reply.readException();
_result = _reply.readDouble();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setOperant(com.example.wongi5.aidlserver.Multiplication multi) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((multi!=null)) {
_data.writeInt(1);
multi.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_setOperant, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setOperant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public java.lang.String getMessage(java.lang.String name) throws android.os.RemoteException;
public double getResult() throws android.os.RemoteException;
public void setOperant(com.example.wongi5.aidlserver.Multiplication multi) throws android.os.RemoteException;
}
