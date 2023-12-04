<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="siac"%>
                           
                           <s:hidden name="idx"/>
                     
                           <table class="table tab_left table-hover dataTable" id="tabellaRipartizioneMutuo" summary="....">
                              <thead>
                                 <tr>
                                    <th scope="col">Tipo ripartizione</th>
                                    <th scope="col">Capitolo</th>
                                    <th scope="col">Importo</th>
                                    <th scope="col">Percentuale</th>
                                    <th scope="col">&nbsp;</th>
                                 </tr>
                              </thead>
                              <tbody>
                                 <s:iterator value="mutuo.elencoRipartizioneMutuo" var="rip" status="status">
                                    <tr>
                                       <td><s:property value="#rip.tipoRipartizioneMutuo.descrizione" /></td>
                                       <td><s:property value="#rip.capitolo.annoCapitoloArticolo" /></td>
                                       <td><s:property value="#rip.ripartizioneImporto" /></td>
                                       <td><siac:numeric value="%{#rip.ripartizionePercentuale}" decimalPlaces="7"/></td>
                                       <td><button data-idx="<s:property value='#status.index' />" type="button" class="eliminaRipartizione"><i class="icon-trash icon-2x"></i></button></td>
                                    </tr>
                                 </s:iterator>
                              </tbody>
                              <tfoot>
                                 <tr>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">Totale interessi</th>
                                    <th scope="col"><s:property value="mutuo.totaleRipartizioneInteressiImporto" /></th>
                                    <th scope="col"><s:property value="mutuo.totaleRipartizioneInteressiPercentuale" /></th>
                                    <th scope="col">&nbsp;</th>
                                 </tr>
                              </tfoot>
                           </table>
