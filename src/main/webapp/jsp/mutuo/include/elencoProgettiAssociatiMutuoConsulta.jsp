<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="siac"%>
                           
                           <table class="table tab_left table-hover dataTable" id="tabellaProgettiAssociatiMutuo" summary="....">
                              <thead>
                                 <tr>
                                    <th scope="col">Codice</th>
                                    <th scope="col">Ambito</th>
                                    <th scope="col">Provvedimento</th>
                                    <th scope="col">Valore iniziale</th>
                                    <th scope="col">Valore attuale</th>
                                 </tr>
                              </thead>
                              <tbody>
                                 <s:iterator value="mutuo.elencoProgettiAssociati" var="progettoAssociatoMutuo" status="status">
                                    <tr>
                                       <td><s:property value="#progettoAssociatoMutuo.progetto.codice" /></td>
                                       <td><s:property value="#progettoAssociatoMutuo.progetto.tipoAmbito.descrizione" /></td>
                                       <td><s:property value="#progettoAssociatoMutuo.progetto.attoAmministrativo.descrizioneCompleta" /></td>
                                       <td><s:property value="#progettoAssociatoMutuo.importoIniziale" /></td>
                                       <td><s:property value="#progettoAssociatoMutuo.progetto.valoreComplessivo" /></td>
                                    </tr>
                                 </s:iterator>
                              </tbody>
                              <tfoot>
                                 <tr>
                                    <th colspan="3" scope="col">&nbsp;</th>
                                    <th class="text-right" scope="col">Totale valore iniziale</th>
                                    <th colspan="2" scope="col" ><s:property value="mutuo.totaleProgettiAssociatiIniziale" /></th>
                                 </tr>
                                 <tr>
                                    <th colspan="3" scope="col">&nbsp;</th>
                                    <th class="text-right" scope="col">Totale valore attuale</th>
                                    <th colspan="2" scope="col" ><s:property value="mutuo.totaleProgettiAssociatiAttuale" /></th>
                                 </tr>
                                 <tr>
                                    <th colspan="3" scope="col">&nbsp;</th>
                                    <th class="text-right" scope="col">Importo mutuo - totale progetti</th>
                                    <th colspan="2" scope="col" ><s:property value="mutuo.diffSommaMutuataProgettiAssociati" /></th>
                                 </tr>
                              </tfoot>
                           </table>
