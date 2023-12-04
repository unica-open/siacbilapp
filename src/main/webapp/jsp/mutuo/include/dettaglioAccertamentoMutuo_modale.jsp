<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="msgDettaglioAccertamento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgDettaglioLabel" aria-hidden="true">
	<div class="modal-body" style="max-height: max-content">
		<div>
            <h4>Accertamento</h4>
               <div class="control-group">
                  <label class="control-label">Anno</label>
                  <div class="controls">
                     <input id="annoAccertamentoDettaglio" type="text" class="lbTextSmall span2" disabled="disabled"  value=""/>
                     <span class="al"> <label class="radio inline" for="statoMutuo">Numero</label></span>
                     <input id="numeroAccertamentoDettaglio" type="text" class="lbTextSmall span2" disabled="disabled" value=""/>
                  </div>
               </div>

               <table class="table tab_left table-hover dataTable" id="tabellaDettaglioAccertamentoMutuo" summary="....">
                  <thead>
                     <tr>
                        <th scope="col">Bilancio</th>
                        <th scope="col">Importo iniziale</th>
                        <th scope="col">Importo attuale</th>
                        <th scope="col">Importo incassato</th>
                    </tr>
                  </thead>


                  <tbody>
                 </tbody>
               </table>

		</div>
	</div>
 	<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
	</div>

</div>

