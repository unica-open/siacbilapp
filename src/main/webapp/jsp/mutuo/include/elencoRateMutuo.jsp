<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="siac"%>
                           
                           <s:hidden name="idx"/>
                           <s:hidden name="dataScadenzaStr"/>
                           <s:hidden name="importoTotaleStr"/>
                           <s:hidden name="importoQuotaCapitaleStr"/>
                           <s:hidden name="importoQuotaInteressiStr"/>
                           <s:hidden name="importoQuotaOneriStr"/>
                     
                           <table class="table tab_left table-hover dataTable" id="tabellaRateMutuo" summary="....">
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
                                    <th>
                                    <s:if test="mutuo.pianoAmmortamentoModificabile">
                                       <button data-idx="0" type="button" class="aggiungiRata"><i class="icon-plus icon-2x"></i></button>
                                    </s:if>
                                    <s:else>&nbsp;</s:else>
                                    </th>
                                 </tr>
                              </thead>
                              <tbody>
                                 <s:iterator value="mutuo.elencoRate" var="rata" status="status">
                                    <tr>
                                       <td class="text-center"><s:property value="#rata.numeroRataPiano" /></td>
                                       <td class="text-center"><s:property value="#rata.anno" /></td>
                                       <td class="text-center"><s:property value="#rata.numeroRataAnno" /></td>
                                       <td>
                                          <input type="text" <s:if test="#status.first">disabled="disabled"</s:if><s:if test="not mutuo.pianoAmmortamentoModificabile">readonly="readonly"</s:if> 
                                             class="lbTextSmall span6 datepicker dataScadenza" title="gg/mm/aaaa" placeholder="dd/mm/aaaa" value='<s:property value="%{#rata.dataScadenza}" />'/>
                                       </td>
                                       <td>
                                          <input type="hidden" name="importoTotale" value='<siac:numeric value="%{#rata.importoTotale}" formatted="false"/>' class="importoTotale importoRataHidden"/>
                                          <input readonly="readonly" type="text" class="span6 decimale soloNumeri forzaVirgolaDecimale text-right importoTotale importoRata" value='<s:property value="%{#rata.importoTotale}" />'/>
                                       </td> 
                                       <td>
                                          <input type="hidden" name="importoQuotaCapitale" value='<siac:numeric value="%{#rata.importoQuotaCapitale}" formatted="false"/>' class="importoQuotaCapitale importoRataHidden"/>
                                          <input <s:if test="not mutuo.importiPianoAmmortamentoModificabili">readonly="readonly"</s:if>
                                             type="text" class="span6 decimale soloNumeri forzaVirgolaDecimale text-right importoRata" value='<s:property value="%{#rata.importoQuotaCapitale}" />'/>
                                       </td>
                                       <td>
                                          <input type="hidden" name="importoQuotaInteressi" value='<siac:numeric value="%{#rata.importoQuotaInteressi}" formatted="false"/>' class="importoQuotaInteressi importoRataHidden"/>
                                          <input <s:if test="not mutuo.importiPianoAmmortamentoModificabili">readonly="readonly"</s:if>
                                             type="text" class="span6 decimale soloNumeri forzaVirgolaDecimale text-right importoRata" value='<s:property value="%{#rata.importoQuotaInteressi}" />'/>
                                       </td>
                                       <td>
                                          <input type="hidden" name="importoQuotaOneri" value='<siac:numeric value="%{#rata.importoQuotaOneri}" formatted="false"/>' class="importoQuotaOneri importoRataHidden"/>
                                          <input <s:if test="not mutuo.importiPianoAmmortamentoModificabili">readonly="readonly"</s:if>
                                             type="text" class="span6 decimale soloNumeri forzaVirgolaDecimale text-right importoRata" value='<s:property value="%{#rata.importoQuotaOneri}" />'/>
                                       </td>
                                       <td>
                                          <input data-raw-value="<siac:numeric value="%{#rata.debitoIniziale}" formatted="false"/>"
                                             type="text" disabled="disabled" class="span6 decimale soloNumeri forzaVirgolaDecimale text-right debitoIniziale" value='<s:property value="%{#rata.debitoIniziale}" />'/>
                                       </td>
                                       <td>
                                          <input data-raw-value="<siac:numeric value="%{#rata.debitoResiduo}" formatted="false"/>"
                                           type="text" disabled="disabled" class="span6 decimale soloNumeri forzaVirgolaDecimale text-right debitoResiduo" value='<s:property value="%{#rata.debitoResiduo}" />'/>
                                       </td>
                                       <td>
                                       <s:if test="mutuo.pianoAmmortamentoModificabile">
                                          <button data-idx="<s:property value='#status.count' />" type="button" class="aggiungiRata"><i class="icon-plus icon-2x"></i></button>
                                          <button data-idx="<s:property value='#status.index' />" type="button" class="eliminaRata"><i class="icon-trash icon-2x"></i></button>
                                       </s:if>
                                       <s:else>&nbsp;</s:else>
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
                                 </tr>
                              </tfoot>
                           </table>
