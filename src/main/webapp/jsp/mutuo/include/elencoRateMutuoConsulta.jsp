<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="siac"%>
                     
                           <table class="table tab_left table-hover dataTable" id="tabellaRateMutuoConsulta" summary="....">
                              <thead>
                                 <tr>
                                    <th scope="col">Numero rata</th>
                                    <th scope="col">Anno</th>
                                    <th scope="col">Numero rata anno</th>
                                    <th scope="col">Data scadenza</th>
                                    <th scope="col">Importo rata</th>
                                    <th scope="col">Quota capitale</th>
                                    <th scope="col">Quota interessi</th>
                                    <th scope="col">Quota oneri</th>
                                    <th scope="col">Debito iniziale</th>
                                    <th scope="col">Debito residuo</th>
                                    
                                 </tr>
                              </thead>
                              <tbody>
                                 <s:iterator value="mutuo.elencoRate" var="rata" status="status">
                                    <tr>
                                       <td ><s:property value="#rata.numeroRataPiano" /></td>
                                       <td ><s:property value="#rata.anno" /></td>
                                       <td ><s:property value="#rata.numeroRataAnno" /></td>
                                       <td><s:property value="%{#rata.dataScadenza}" />
                                       </td>
                                       <td><s:property value="%{#rata.importoTotale}" />
                                       </td> 
                                       <td><s:property value="%{#rata.importoQuotaCapitale}" />
                                       </td>
                                       <td><s:property value="%{#rata.importoQuotaInteressi}" />
                                       </td>
                                       <td><s:property value="%{#rata.importoQuotaOneri}" />
                                       </td>
                                       <td><s:property value="%{#rata.debitoIniziale}" />
                                       </td>
                                       <td><s:property value="%{#rata.debitoResiduo}" />
                                       </td>
                                       
                                    </tr>
                                 </s:iterator>
                              </tbody>
                              <tfoot>
                                 <tr>
                                    <th scope="col">Totale</th>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">&nbsp;</th>
                                    <th class="totale-importoTotale" id="totaleImportoRata" scope="col"><s:property value="mutuo.totaleImportoRata" /></th>
                                    <th class="totale-importoQuotaCapitale" id="totaleImportoQuotaCapitaleRata" scope="col"><s:property value="mutuo.totaleImportoQuotaCapitaleRata" /></th>
                                    <th class="totale-importoQuotaInteressi" id="totaleImportoQuotaInteressiRata" scope="col"><s:property value="mutuo.totaleImportoQuotaInteressiRata" /></th>
                                    <th class="totale-importoQuotaOneri" id="totaleImportoQuotaOneriRata" scope="col"><s:property value="mutuo.totaleImportoQuotaOneriRata" /></th>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">&nbsp;</th>
                                 </tr>  </tfoot>
                           </table>
                           