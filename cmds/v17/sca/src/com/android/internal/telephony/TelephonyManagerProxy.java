/*
 * Copyright (c) 2016 Nick Guo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.internal.telephony;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.CellInfo;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.NeighboringCellInfo;

import com.nick.commands.sca.Sca;

import java.util.List;

public class TelephonyManagerProxy extends ITelephony.Stub {

    ITelephony mService;

    public TelephonyManagerProxy() {
        mService = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        Sca.log("TelephonyManagerProxy startup, mService=" + mService);
    }

    @Override
    public void dial(String number) throws RemoteException {

    }

    @Override
    public void call(String callingPackage, String number) throws RemoteException {

    }

    @Override
    public boolean endCall() throws RemoteException {
        return false;
    }

    @Override
    public boolean endCallForSubscriber(int subId) throws RemoteException {
        return false;
    }

    @Override
    public void answerRingingCall() throws RemoteException {

    }

    @Override
    public void answerRingingCallForSubscriber(int subId) throws RemoteException {

    }

    @Override
    public void silenceRinger() throws RemoteException {

    }

    @Override
    public boolean isOffhook() throws RemoteException {
        return false;
    }

    @Override
    public boolean isOffhookForSubscriber(int subId) throws RemoteException {
        return false;
    }

    @Override
    public boolean isRingingForSubscriber(int subId) throws RemoteException {
        return false;
    }

    @Override
    public boolean isRinging() throws RemoteException {
        return false;
    }

    @Override
    public boolean isIdle() throws RemoteException {
        return false;
    }

    @Override
    public boolean isIdleForSubscriber(int subId) throws RemoteException {
        return false;
    }

    @Override
    public boolean isRadioOn() throws RemoteException {
        return false;
    }

    @Override
    public boolean isRadioOnForSubscriber(int subId) throws RemoteException {
        return false;
    }

    @Override
    public boolean isSimPinEnabled() throws RemoteException {
        return false;
    }

    @Override
    public boolean supplyPin(String pin) throws RemoteException {
        return false;
    }

    @Override
    public boolean supplyPinForSubscriber(int subId, String pin) throws RemoteException {
        return false;
    }

    @Override
    public boolean supplyPuk(String puk, String pin) throws RemoteException {
        return false;
    }

    @Override
    public boolean supplyPukForSubscriber(int subId, String puk, String pin) throws RemoteException {
        return false;
    }

    @Override
    public int[] supplyPinReportResult(String pin) throws RemoteException {
        return new int[0];
    }

    @Override
    public int[] supplyPinReportResultForSubscriber(int subId, String pin) throws RemoteException {
        return new int[0];
    }

    @Override
    public int[] supplyPukReportResult(String puk, String pin) throws RemoteException {
        return new int[0];
    }

    @Override
    public int[] supplyPukReportResultForSubscriber(int subId, String puk, String pin) throws RemoteException {
        return new int[0];
    }

    @Override
    public boolean handlePinMmi(String dialString) throws RemoteException {
        return false;
    }

    @Override
    public boolean handlePinMmiForSubscriber(int subId, String dialString) throws RemoteException {
        return false;
    }

    @Override
    public void toggleRadioOnOff() throws RemoteException {

    }

    @Override
    public void toggleRadioOnOffForSubscriber(int subId) throws RemoteException {

    }

    @Override
    public boolean setRadio(boolean turnOn) throws RemoteException {
        Sca.log("setRadio:" + turnOn + ", calling uid:" + Binder.getCallingUid());
        Binder.clearCallingIdentity();
        return mService.setRadioPower(turnOn);
    }

    @Override
    public boolean setRadioForSubscriber(int subId, boolean turnOn) throws RemoteException {
        return false;
    }

    @Override
    public boolean setRadioPower(boolean turnOn) throws RemoteException {
        return false;
    }

    @Override
    public void updateServiceLocation() throws RemoteException {

    }

    @Override
    public void updateServiceLocationForSubscriber(int subId) throws RemoteException {

    }

    @Override
    public void enableLocationUpdates() throws RemoteException {

    }

    @Override
    public void enableLocationUpdatesForSubscriber(int subId) throws RemoteException {

    }

    @Override
    public void disableLocationUpdates() throws RemoteException {

    }

    @Override
    public void disableLocationUpdatesForSubscriber(int subId) throws RemoteException {

    }

    @Override
    public boolean enableDataConnectivity() throws RemoteException {
        return false;
    }

    @Override
    public boolean disableDataConnectivity() throws RemoteException {
        return false;
    }

    @Override
    public boolean isDataConnectivityPossible() throws RemoteException {
        return false;
    }

    @Override
    public boolean isDataPossibleForSubscription(int subId, String apnType) throws RemoteException {
        return false;
    }

    @Override
    public Bundle getCellLocation() throws RemoteException {
        return null;
    }

    @Override
    public List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg) throws RemoteException {
        return null;
    }

    @Override
    public int getCallState() throws RemoteException {
        return 0;
    }

    @Override
    public int getCallStateForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public int getDataActivity() throws RemoteException {
        return 0;
    }

    @Override
    public int getDataState() throws RemoteException {
        return 0;
    }

    @Override
    public int getActivePhoneType() throws RemoteException {
        return 0;
    }

    @Override
    public int getActivePhoneTypeForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public int getCdmaEriIconIndex() throws RemoteException {
        return 0;
    }

    @Override
    public int getCdmaEriIconIndexForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public int getCdmaEriIconMode() throws RemoteException {
        return 0;
    }

    @Override
    public int getCdmaEriIconModeForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public String getCdmaEriText() throws RemoteException {
        return null;
    }

    @Override
    public String getCdmaEriTextForSubscriber(int subId) throws RemoteException {
        return null;
    }

    @Override
    public boolean needsOtaServiceProvisioning() throws RemoteException {
        return false;
    }

    @Override
    public boolean setVoiceMailNumber(int subId, String alphaTag, String number) throws RemoteException {
        return false;
    }

    @Override
    public int getVoiceMessageCount() throws RemoteException {
        return 0;
    }

    @Override
    public int getVoiceMessageCountForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public int getNetworkType() throws RemoteException {
        return 0;
    }

    @Override
    public int getNetworkTypeForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public int getDataNetworkType() throws RemoteException {
        return 0;
    }

    @Override
    public int getDataNetworkTypeForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public int getVoiceNetworkType() throws RemoteException {
        return 0;
    }

    @Override
    public int getVoiceNetworkTypeForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public String getIccOperatorNumeric(int subId) throws RemoteException {
        return null;
    }

    @Override
    public boolean hasIccCard() throws RemoteException {
        return false;
    }

    @Override
    public boolean hasIccCardUsingSlotId(int slotId) throws RemoteException {
        return false;
    }

    @Override
    public int getLteOnCdmaMode() throws RemoteException {
        return 0;
    }

    @Override
    public int getLteOnCdmaModeForSubscriber(int subId) throws RemoteException {
        return 0;
    }

    @Override
    public List<CellInfo> getAllCellInfo() throws RemoteException {
        return null;
    }

    @Override
    public List<CellInfo> getAllCellInfoUsingSubId(int subId) throws RemoteException {
        return null;
    }

    @Override
    public void setCellInfoListRate(int rateInMillis) throws RemoteException {

    }

    @Override
    public int getDefaultSim() throws RemoteException {
        return 0;
    }

    @Override
    public IccOpenLogicalChannelResponse iccOpenLogicalChannel(String AID) throws RemoteException {
        return null;
    }

    @Override
    public IccOpenLogicalChannelResponse iccOpenLogicalChannelUsingSubId(int subId, String AID) throws RemoteException {
        return null;
    }

    @Override
    public boolean iccCloseLogicalChannel(int channel) throws RemoteException {
        return false;
    }

    @Override
    public boolean iccCloseLogicalChannelUsingSubId(int subId, int channel) throws RemoteException {
        return false;
    }

    @Override
    public String iccTransmitApduLogicalChannel(int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
        return null;
    }

    @Override
    public String iccTransmitApduLogicalChannelUsingSubId(int subId, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
        return null;
    }

    @Override
    public String iccTransmitApduBasicChannel(int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
        return null;
    }

    @Override
    public String iccTransmitApduBasicChannelUsingSubId(int subId, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
        return null;
    }

    @Override
    public byte[] iccExchangeSimIO(int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
        return new byte[0];
    }

    @Override
    public byte[] iccExchangeSimIOUsingSubId(int subId, int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
        return new byte[0];
    }

    @Override
    public String sendEnvelopeWithStatus(String content) throws RemoteException {
        return null;
    }

    @Override
    public String nvReadItem(int itemID) throws RemoteException {
        return null;
    }

    @Override
    public boolean nvWriteItem(int itemID, String itemValue) throws RemoteException {
        return false;
    }

    @Override
    public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) throws RemoteException {
        return false;
    }

    @Override
    public boolean nvResetConfig(int resetType) throws RemoteException {
        return false;
    }

    @Override
    public int getCalculatedPreferredNetworkType() throws RemoteException {
        return 0;
    }

    @Override
    public int getPreferredNetworkType() throws RemoteException {
        return 0;
    }

    @Override
    public int getTetherApnRequired() throws RemoteException {
        return 0;
    }

    @Override
    public boolean setPreferredNetworkType(int networkType) throws RemoteException {
        return false;
    }

    @Override
    public void setDataEnabled(int subId, boolean enable) throws RemoteException {

    }

    @Override
    public boolean getDataEnabled(int subId) throws RemoteException {
        return false;
    }

    @Override
    public String[] getPcscfAddress(String apnType) throws RemoteException {
        return new String[0];
    }

    @Override
    public void setImsRegistrationState(boolean registered) throws RemoteException {

    }

    @Override
    public String getCdmaMdn(int subId) throws RemoteException {
        return null;
    }

    @Override
    public String getCdmaMin(int subId) throws RemoteException {
        return null;
    }

    @Override
    public int getCarrierPrivilegeStatus() throws RemoteException {
        return 0;
    }

    @Override
    public int checkCarrierPrivilegesForPackage(String pkgname) throws RemoteException {
        return 0;
    }

    @Override
    public List<String> getCarrierPackageNamesForIntent(Intent intent) throws RemoteException {
        return null;
    }

    @Override
    public boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) throws RemoteException {
        return false;
    }

    @Override
    public String getLine1NumberForDisplay(int subId) throws RemoteException {
        return null;
    }

    @Override
    public String getLine1AlphaTagForDisplay(int subId) throws RemoteException {
        return null;
    }

    @Override
    public boolean setOperatorBrandOverride(String brand) throws RemoteException {
        return false;
    }

    @Override
    public boolean setRoamingOverride(List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) throws RemoteException {
        return false;
    }

    @Override
    public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
        return 0;
    }

    @Override
    public byte[] getAtr() throws RemoteException {
        return new byte[0];
    }

    @Override
    public byte[] getAtrUsingSubId(int subId) throws RemoteException {
        return new byte[0];
    }

    @Override
    public boolean needMobileRadioShutdown() throws RemoteException {
        return false;
    }

    @Override
    public void shutdownMobileRadios() throws RemoteException {

    }

    @Override
    public void enableVideoCalling(boolean enable) throws RemoteException {

    }

    @Override
    public boolean isVideoCallingEnabled() throws RemoteException {
        return false;
    }

    @Override
    public String getDeviceId() throws RemoteException {
        return null;
    }
}
