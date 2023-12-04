<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	
	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>
                  <s:form id="formRisultatiRicercaAccertamentoMutuo" cssClass="form-horizontal" >
                       <s:hidden name="mutuo.uid" />
 		         		
						<h3><span id="id_num_result" class="num_result"></span></h3>
                           <s:hidden name="idx"/>
                     
                           <table class="table tab_left table-hover dataTable" id="tabellaRisultatiRicercaAccertamentoMutuo" summary="....">
                              <thead>
                                 <tr>
                                	<th scope="col"></th>
                                    <th scope="col">Anno</th>
                                    <th scope="col">Numero</th>
                                    <th scope="col">Stato</th>
                                    <th scope="col">Titolo</th>
                                    <th scope="col">Cap/Art</th>
                                    <th scope="col">Tipo Finanziamento</th>
                                    <th scope="col">Provvedimento</th>
                                    <th scope="col">Tipo</th>
                                    <th scope="col">Struttura amministrativa</th>
                                    <th scope="col">Soggetto/Classe</th>
                                    <th scope="col">Sub</th>
                                    <th scope="col">Importo Movimento</th>
	                             </tr>
                              </thead>
                              <tbody>
							  </tbody>
                           </table>
                        
						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						
						<p>
						 <s:submit value="indietro" cssClass="btn" action="risultatiRicercaAccertamentoMutuo_indietro" />
						 <s:submit value="movimenti contabili" cssClass="btn" action="movimentiGestioneAssociatiMutuo" />
 						 <button type="button" disabled="disabled" id="associaMovimentiSelezionati" class="btn selezionati">associa movimenti selezionati</button>
						</p>  
                  
                  
					</s:form>
				</div>	
			</div>	
		</div>	 
	</div>	

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/mutuo/risultatiRicercaAccertamentoMutuo.js"></script>
</body>
</html>