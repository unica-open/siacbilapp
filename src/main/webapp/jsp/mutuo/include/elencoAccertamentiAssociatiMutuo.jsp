<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="siac"%>
                           
                           <table class="table tab_left table-hover dataTable" id="tabellaAccertamentiAssociatiMutuo" summary="....">
                              <thead>
                                 <tr>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">Anno</th>
                                    <th scope="col">Numero</th>
                                    <th scope="col">Stato</th>
                                    <th scope="col">Titolo</th>
                                    <th scope="col">Capitolo</th>
                                    <th scope="col">Tipo finanziamento</th>
                                    <th scope="col">Provvedimento</th>
                                    <th scope="col">Soggetto</th>
                                    <th scope="col">Sub</th>
                                    <th scope="col">Altri mutui</th>
                                    <th scope="col">Importo attuale</th>
                                    <th scope="col">Importo incassato</th>
                                    <th scope="col">Dettaglio</th>
                                 </tr>
                              </thead>
                              <tbody>
                                 <s:iterator value="mutuo.elencoAccertamentiAssociati" var="accertamentoAssociatoMutuo" status="status">
                                    <tr>
                                       <td><input type="checkbox" class="idMovimentoGestione" name="elencoIdMovimentiGestione" value='<s:property value="#accertamentoAssociatoMutuo.accertamento.uid"/>' /> </td>
                                       <td class="annoMovimentoGestione"><s:property value="#accertamentoAssociatoMutuo.accertamento.anno" /></td>
                                       <td class="numeroMovimentoGestione"><s:property value="#accertamentoAssociatoMutuo.accertamento.numero" /></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.statoOperativo.descrizione" /></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.capitolo.titoloEntrata.codice" /></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.capitolo.annoCapitoloArticolo" /></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.capitolo.tipoFinanziamento.descrizione" /></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.attoAmministrativo.descrizioneCompleta" /></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.soggetto.codiceDenominazione" /></td>
                                       <td><s:if test="#accertamentoAssociatoMutuo.accertamento.totaleSubAccertamenti gt 0">S&igrave;</s:if><s:else>No</s:else></td>
                                       <td class="text-center"><s:if test="#accertamentoAssociatoMutuo.accertamento.totaleMutuiAssociati gt 1"><i class="icon-check"></i></s:if></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.importoAttuale" /></td>
                                       <td><s:property value="#accertamentoAssociatoMutuo.accertamento.importoIncassato" /></td>
                                       <td><a href='#msgDettaglioAccertamento' data-toggle='modal' class="dettaglioAccertamento"><i class="icon-table icon-2x"></i></a></td>
                                    </tr>
                                 </s:iterator>
                              </tbody>
                              <tfoot>
                                 <tr>
                                    <th colspan="8" scope="col">&nbsp;</th>
                                    <th class="text-right" scope="col">Totale movimenti entrata</th>
                                    <th scope="col"><s:property value="mutuo.totaleAccertamentiAssociati" /></th>
                                    <th colspan="4" scope="col">&nbsp;</th>
                                 </tr>
                                 <tr>
                                    <th colspan="8" scope="col">&nbsp;</th>
                                    <th class="text-right" scope="col">Importo mutuo ancora da associare</th>
                                    <th scope="col"><s:property value="mutuo.diffSommaMutuataAccertamentiAssociati" /></th>
                                    <th colspan="4" scope="col">&nbsp;</th>
                                 </tr>
                              </tfoot>
                           </table>
