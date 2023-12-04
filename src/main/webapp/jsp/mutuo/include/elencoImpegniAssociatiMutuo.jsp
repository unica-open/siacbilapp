<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="siac"%>
                           
                           <table class="table tab_left table-hover dataTable" id="tabellaImpegniAssociatiMutuo" summary="....">
                              <thead>
                                 <tr>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">Anno</th>
                                    <th scope="col">Numero</th>
                                    <th scope="col">Stato</th>
                                    <th scope="col">Missione</th>
                                    <th scope="col">Programma</th>
                                    <th scope="col">Capitolo</th>
                                    <th scope="col">Tipo finanziamento</th>
                                    <th scope="col">Componente</th>
                                    <th scope="col">Provvedimento</th>
                                    <th scope="col">Soggetto</th>
                                    <th scope="col">CIG</th>
                                    <th scope="col">CUP</th>
                                    <th scope="col">Sub</th>
                                    <th scope="col">Altri mutui</th>
                                    <th scope="col">Importo attuale</th>
                                    <th scope="col">Importo liquidato</th>
                                    <th scope="col">Dettaglio</th>
                                 </tr>
                              </thead>
                              <tbody>
                                 <s:iterator value="mutuo.elencoImpegniAssociati" var="impegnoAssociatoMutuo" status="status">
                                    <tr>
                                       <td><input type="checkbox" class="idMovimentoGestione" name="elencoIdMovimentiGestione" value='<s:property value="#impegnoAssociatoMutuo.impegno.uid"/>' /> </td>
                                       <td class="annoMovimentoGestione"><s:property value="#impegnoAssociatoMutuo.impegno.anno" /></td>
                                       <td class="numeroMovimentoGestione"><s:property value="#impegnoAssociatoMutuo.impegno.numero" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.statoOperativo.descrizione" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.capitolo.missione.codice" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.capitolo.programma.codice" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.capitolo.annoCapitoloArticolo" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.capitolo.tipoFinanziamento.descrizione" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.componenteBilancioImpegno.descrizioneTipoComponente" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.attoAmministrativo.descrizioneCompleta" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.soggetto.codiceDenominazione" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.cig" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.cup" /></td>
                                       <td><s:if test="#impegnoAssociatoMutuo.impegno.totaleSubImpegni gt 0">S&igrave;</s:if><s:else>No</s:else></td>
                                       <td class="text-center"><s:if test="#impegnoAssociatoMutuo.impegno.totaleMutuiAssociati gt 1"><i class="icon-check"></i></s:if></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.importoAttuale" /></td>
                                       <td><s:property value="#impegnoAssociatoMutuo.impegno.importoLiquidato" /></td>
                                       <td><a href='#msgDettaglio' data-toggle='modal' class="dettaglio"><i class="icon-table icon-2x"></i></a></td>
                                    </tr>
                                 </s:iterator>
                              </tbody>
                              <tfoot>
                                 <tr>
                                    <th colspan="10" scope="col">&nbsp;</th>
                                    <th class="text-right" colspan="2" scope="col">Totale movimenti spesa</th>
                                    <th scope="col"><s:property value="mutuo.totaleImpegniAssociati" /></th>
                                    <th colspan="5" scope="col">&nbsp;</th>
                                 </tr>
                                 <tr>
                                    <th colspan="10" scope="col">&nbsp;</th>
                                    <th class="text-right" colspan="2" scope="col">Importo mutuo ancora da associare</th>
                                    <th scope="col"><s:property value="mutuo.diffSommaMutuataImpegniAssociati" /></th>
                                    <th colspan="5" scope="col">&nbsp;</th>
                                 </tr>
                              </tfoot>
                           </table>
